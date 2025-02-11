package reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SchedulerTest {

    private Flux<Integer> producer() {
        return Flux.create(fluxSink -> {
            Thread thread = new Thread(() -> {
                IntStream
                        .range(0, 5)
                        .forEach(i -> {
                            System.out.println("producer start: " + i);
                            fluxSink.next(i);
                            System.out.println("producer end: " + i);
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
        System.out.println("start");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        producer()
                .publishOn(Schedulers.newSingle("handle-1"))
                .map(i -> {
                    System.out.println("[" + Thread.currentThread() + "]handle-1: " + i);
                    return i;
                })
                .publishOn(Schedulers.newSingle("handle-2"))
                .map(i -> {
                    System.out.println("[" + Thread.currentThread() + "]handle-2: " + i);
                    return i;
                })
                .subscribe(message -> System.out.println("subscribe: " + message), System.err::println, countDownLatch::countDown);
        System.out.println("end");
        countDownLatch.await();
    }

    @Test
    public void subscribeOn() throws InterruptedException {
        Flux<String> flux = Flux.create(fluxSink -> {
            for (int i = 0; i < 20; i++) {
                fluxSink.next(Thread.currentThread().getName() + ": " + i);
            }
            fluxSink.complete();
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        flux
                .subscribeOn(Schedulers.newSingle("subscribe"))
                .subscribe(System.out::println, System.err::println, countDownLatch::countDown);
        System.out.println("end");
        countDownLatch.await();
        System.out.println("exit");
    }

    @Test
    public void sleep() throws InterruptedException {
        System.out.println("start");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        producer()
                .publishOn(Schedulers.newSingle("handle-1"))
                .map(i -> {
                    System.out.println("handle-1: " + i);
                    try {
                        TimeUnit.MILLISECONDS.sleep(500 - i * 100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return i;
                })
                .publishOn(Schedulers.newSingle("handle-2"))
                .map(i -> {
                    System.out.println("handle-2: " + i);
                    try {
                        TimeUnit.MILLISECONDS.sleep(500 - i * 100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return i;
                })
                .subscribe(message -> System.out.println("subscribe: " + message), System.err::println, countDownLatch::countDown);
        System.out.println("end");
        countDownLatch.await();
    }

    @Test
    public void boundedElastic() throws InterruptedException {
        Scheduler scheduler = Schedulers.newBoundedElastic(8, 16, "boundedElastic");
        int size = 16;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            int index = i;
            Flux.range(0, 8)
                    .publishOn(scheduler)
                    .map(integer -> "[" + index + "] " + Thread.currentThread().getName() + ":" + integer)
                    .subscribe(System.out::println, System.err::println, countDownLatch::countDown);
        }
        countDownLatch.await();
    }

    @Test
    public void tooMuchTask() throws InterruptedException {
        int queuedTaskCap = 4;
        Scheduler scheduler = Schedulers.newBoundedElastic(2, queuedTaskCap, "boundedElastic");
        int size = queuedTaskCap << 4;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            int index = i;
            Flux.range(0, 8)
                    .publishOn(scheduler)
                    .map(integer -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(8);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        return "[" + index + "] " + Thread.currentThread().getName() + ":" + integer;
                    })
                    .subscribe(System.out::println, x -> {
                        System.err.println("[" + index + "] " + x);
                        countDownLatch.countDown();
                    }, countDownLatch::countDown);
        }
        countDownLatch.await();
    }


}
