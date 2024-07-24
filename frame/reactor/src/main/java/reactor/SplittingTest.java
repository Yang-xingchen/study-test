package reactor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class SplittingTest {

    private static final Logger log = LogManager.getLogger(SplittingTest.class);

    @Test
    public void group() {
        Flux.range(0, 50)
                .groupBy(i -> i & 0x7)
                .subscribe(flux -> {
                    log.info("group-{} subscribe", flux.key());
                    flux.subscribe(i -> log.info("group-{}: {}", flux.key(), i));
                });
    }

    @Test
    public void window() {
        log.info("Flux#window(int)");
        Flux.range(0, 50)
                .window(8)
                .subscribe(flux -> {
                    log.info("flux subscribe");
                    flux.collectList().subscribe(log::info);
//                    flux.subscribe(log::info);
                });
        log.info("Flux#window(int, int)");
        Flux.range(0, 50)
                .window(8, 4)
                .subscribe(flux -> {
                    log.info("flux subscribe");
                    flux.collectList().subscribe(log::info);
//                    flux.subscribe(log::info);
                });
    }

    @Test
    public void windowUtil() {
        Flux.range(0, 50)
                .windowUntil(i -> (i & 0x7) == 0, true)
                .subscribe(flux -> {
                    log.info("flux subscribe");
                    flux.collectList().subscribe(log::info);
//                    flux.subscribe(log::info);
                });
    }

    @Test
    public void buffer() {
        log.info("Flux#buffer(int)");
        Flux.range(0, 50)
                .buffer(8)
                .subscribe(log::info);
        log.info("Flux#buffer(int, int)");
        Flux.range(0, 50)
                .buffer(8, 4)
                .subscribe(log::info);
    }

    @Test
    public void bufferUtil() {
        Flux.range(0, 50)
                .bufferUntil(i -> (i & 0x7) == 0, true)
                .subscribe(log::info);
    }

}
