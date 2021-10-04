package reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Broadcast {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    /**
     * {@link ConnectableFlux#connect()} 后直接开始生产，若无订阅则丢弃
     */
    @Test
    public void test() throws InterruptedException {
        ConnectableFlux<Integer> connectableFlux = Flux.create(this::create).publish();
        connectableFlux.connect();
        for (int i = 0; i < 3; i++) {
            TimeUnit.SECONDS.sleep(3);
            int finalI = i;
            connectableFlux
//                    .publishOn(Schedulers.newSingle("single"))
                    .subscribe(integer ->
                            System.out.println(Thread.currentThread().getName() + " subscribe-" + finalI + ": " + integer));
            System.out.println("> subscribe " + i);
        }
        TimeUnit.SECONDS.sleep(15);
        System.out.println("> end");
    }

    /**
     * 有订阅后开始生产
     */
    @Test
    public void test2() throws InterruptedException {
        Flux<Integer> flux = Flux.create(this::create).publish().autoConnect();
        for (int i = 0; i < 3; i++) {
            TimeUnit.SECONDS.sleep(3);
            int finalI = i;
            flux
//                    .publishOn(Schedulers.newSingle("single"))
                    .subscribe(integer ->
                            System.out.println(Thread.currentThread().getName() + " subscribe-" + finalI + ": " + integer));
            System.out.println("> subscribe " + i);
        }
        TimeUnit.SECONDS.sleep(15);
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
}
