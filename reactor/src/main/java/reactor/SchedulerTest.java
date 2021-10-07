package reactor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SchedulerTest {

    private static final Logger log = LogManager.getLogger(SchedulerTest.class);

    private Flux<Integer> producer() {
        return Flux.create(fluxSink -> {
            Thread thread = new Thread(() -> {
                IntStream
                        .range(0, 5)
                        .forEach(i -> {
                            log.info("producer start: " + i);
                            fluxSink.next(i);
                            log.info("producer end: " + i);
                            try {
                                TimeUnit.MILLISECONDS.sleep(50);
                            } catch (InterruptedException e) {
                                fluxSink.error(e);
                            }
                        });
                fluxSink.complete();
            });
            thread.setName("producer");
            thread.start();
        });
    }

    @Test
    public void publishOn() throws InterruptedException {
        log.info("start");
        producer()
                .publishOn(Schedulers.newSingle("handle-1"))
                .map(i -> {
                    log.info("handle-1: " + i);
                    return i;
                })
                .publishOn(Schedulers.newSingle("handle-2"))
                .map(i -> {
                    log.info("handle-2: " + i);
                    return i;
                })
                .subscribe(message -> log.info("subscribe: " + message));
        log.info("end");
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void sleep() throws InterruptedException {
        log.info("start");
        producer()
                .publishOn(Schedulers.newSingle("handle-1"))
                .map(i -> {
                    log.info("handle-1: " + i);
                    try {
                        TimeUnit.MILLISECONDS.sleep(500 - i * 100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return i;
                })
                .publishOn(Schedulers.newSingle("handle-2"))
                .map(i -> {
                    log.info("handle-2: " + i);
                    try {
                        TimeUnit.MILLISECONDS.sleep(500 - i * 100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return i;
                })
                .subscribe(message -> log.info("subscribe: " + message));
        log.info("end");
        TimeUnit.SECONDS.sleep(3);
    }

}
