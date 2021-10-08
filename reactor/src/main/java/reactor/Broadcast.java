package reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Broadcast {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    /**
     * {@link ConnectableFlux#connect()} 后直接开始生产，若无订阅则丢弃
     */
    @Test
    public void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        ConnectableFlux<Integer> connectableFlux = Flux.create(this::create).publish();
        connectableFlux.connect();
        for (int i = 0; i < 3; i++) {
            TimeUnit.SECONDS.sleep(3);
            int finalI = i;
            connectableFlux
//                    .publishOn(Schedulers.newSingle("single"))
                    .subscribe(integer ->
                            System.out.println(Thread.currentThread().getName() + " subscribe-" + finalI + ": " + integer),
                            System.err::println, countDownLatch::countDown);
            System.out.println("> subscribe " + i);
        }
        countDownLatch.await();
        System.out.println("> end");
    }

    /**
     * 有订阅后开始生产
     */
    @Test
    public void test2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Flux<Integer> flux = Flux.create(this::create).publish().autoConnect();
        for (int i = 0; i < 3; i++) {
            TimeUnit.SECONDS.sleep(3);
            int finalI = i;
            flux
//                    .publishOn(Schedulers.newSingle("single"))
                    .subscribe(integer ->
                            System.out.println(Thread.currentThread().getName() + " subscribe-" + finalI + ": " + integer),
                            System.err::println, countDownLatch::countDown);
            System.out.println("> subscribe " + i);
        }
        countDownLatch.await();
        System.out.println("> end");
    }

    private void create(FluxSink<Integer> fluxSink) {
        new Thread(() -> {
            System.out.println("> start producer");
            for (int i = 0; i < 20; i++) {
                int r = RANDOM.nextInt(900) + 100;
                System.out.println("> producer " + i + ": " + r);
                fluxSink.next(r);
                try {
                    TimeUnit.MILLISECONDS.sleep(r);
                } catch (InterruptedException e) {
                    fluxSink.error(e);
                }
            }
            fluxSink.complete();
            System.out.println("> end producer");
        }).start();
    }

    /**
     * 多次生产，并非广播
     */
    @Test
    public void multiSubscribe() {
        Flux<Integer> range = Flux.range(0, 5).map(integer -> {
            System.out.println("producer: " + integer);
            return integer;
        });
        range.map(i -> "subscribe1: " + i).subscribe(System.out::println);
        System.out.println("next");
        range.map(i -> "subscribe2: " + i).subscribe(System.out::println);
        System.out.println("next");
        range = Flux.range(0, 5);
        range.publishOn(Schedulers.newSingle("subscribe1"))
                .map(i -> Thread.currentThread().getName() + ": " + i)
                .subscribe(System.out::println);
        System.out.println("next");
        range.publishOn(Schedulers.newSingle("subscribe2"))
                .map(i -> Thread.currentThread().getName() + ": " + i)
                .subscribe(System.out::println);
    }

}
