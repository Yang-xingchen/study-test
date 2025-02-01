package reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ParallelTest {

    @Test
    public void base() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<Integer> flux = Flux.range(0, 100);
        ParallelFlux.from(flux)
                .runOn(Schedulers.newParallel("parallel"))
                .map(i -> {
                    System.out.println(i);
                    try {
                        TimeUnit.MILLISECONDS.sleep(100 - i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return i;
                })
                .subscribe(System.out::println, System.err::println, countDownLatch::countDown);
        countDownLatch.await();
    }

}
