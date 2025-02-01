package reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class SplittingTest {

    @Test
    public void group() {
        Flux.range(0, 50)
                .groupBy(i -> i & 0x7)
                .subscribe(flux -> {
                    System.out.println("group-" + flux.key() + " subscribe");
                    flux.subscribe(i -> System.out.println("group-" + flux.key() + ": " + i));
                });
    }

    @Test
    public void window() {
        System.out.println("Flux#window(int)");
        Flux.range(0, 50)
                .window(8)
                .subscribe(flux -> {
                    System.out.println("flux subscribe");
                    flux.collectList().subscribe(System.out::println);
//                    flux.subscribe(System.out::println);
                });
        System.out.println("Flux#window(int, int)");
        Flux.range(0, 50)
                .window(8, 4)
                .subscribe(flux -> {
                    System.out.println("flux subscribe");
                    flux.collectList().subscribe(System.out::println);
//                    flux.subscribe(System.out::println);
                });
    }

    @Test
    public void windowUtil() {
        Flux.range(0, 50)
                .windowUntil(i -> (i & 0x7) == 0, true)
                .subscribe(flux -> {
                    System.out.println("flux subscribe");
                    flux.collectList().subscribe(System.out::println);
//                    flux.subscribe(System.out::println);
                });
    }

    @Test
    public void buffer() {
        System.out.println("Flux#buffer(int)");
        Flux.range(0, 50)
                .buffer(8)
                .subscribe(System.out::println);
        System.out.println("Flux#buffer(int, int)");
        Flux.range(0, 50)
                .buffer(8, 4)
                .subscribe(System.out::println);
    }

    @Test
    public void bufferUtil() {
        Flux.range(0, 50)
                .bufferUntil(i -> (i & 0x7) == 0, true)
                .subscribe(System.out::println);
    }

}
