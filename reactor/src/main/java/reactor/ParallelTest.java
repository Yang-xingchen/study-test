package reactor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ParallelTest {

    private static final Logger log = LogManager.getLogger(SchedulerTest.class);

    @Test
    public void base() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<Integer> flux = Flux.range(0, 100);
        ParallelFlux.from(flux)
                .runOn(Schedulers.newParallel("parallel"))
                .map(i -> {
                    log.info(i);
                    try {
                        TimeUnit.MILLISECONDS.sleep(100 - i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return i;
                })
                .subscribe(log::info, log::error, countDownLatch::countDown);
        countDownLatch.await();
    }

}
