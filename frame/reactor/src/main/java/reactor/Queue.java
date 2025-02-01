package reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class Queue {

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            4, 4,
            1, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10), new ThreadFactory() {
        private final AtomicLong atomicLong = new AtomicLong();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName("consumer-" + atomicLong.getAndIncrement());
            return thread;
        }
    }, new ThreadPoolExecutor.CallerRunsPolicy());
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static void main(String[] args) {
        Flux.create(new Producer())
                .publishOn(Schedulers.newSingle("reactor"))
                .map(s -> {
                    sleep(50, 450);
                    return "handleThread: " + Thread.currentThread().getName() + ", " + s;
                })
                .timed()
                .subscribe(stringTimed -> executor.execute(() -> {
                    System.out.printf("executorThread: %s, %s, %dms%n",
                            Thread.currentThread().getName(), stringTimed.get(), System.currentTimeMillis() - stringTimed.timestamp().toEpochMilli());
                    sleep(400, 100);
                }));
    }

    private static void sleep(int base, int random) {
        try {
            TimeUnit.MILLISECONDS.sleep(base + RANDOM.nextInt(random));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class Producer implements Consumer<FluxSink<String>> {

        private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                8, 8,
                1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50), new ThreadFactory() {
            private final AtomicLong atomicLong = new AtomicLong();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = Executors.defaultThreadFactory().newThread(r);
                thread.setName("producer-" + atomicLong.getAndIncrement());
                return thread;
            }
        }, new ThreadPoolExecutor.AbortPolicy());

        @Override
        public void accept(FluxSink<String> stringFluxSink) {
            CountDownLatch countDownLatch = new CountDownLatch(50);
            for (int i = 0; i < 50; i++) {
                executor.submit(() -> {
                    try {
                        String threadName = Thread.currentThread().getName();
                        for (int j = 0; j < 200; j++) {
                            stringFluxSink.next("producer: " + threadName + ": " + j);
                            TimeUnit.MILLISECONDS.sleep(100 + RANDOM.nextInt(900));
                        }
                    } catch (InterruptedException e) {
                        stringFluxSink.error(e);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                stringFluxSink.error(e);
            }
            stringFluxSink.complete();
        }

    }

}
