package reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    @Test
    public void mono() {
        Mono.just("test")
                .map(String::toUpperCase)
                .subscribe(s -> Assertions.assertEquals("TEST", s));
        Mono.fromSupplier(()->"test")
                .subscribe(s -> Assertions.assertEquals("test", s));
    }

    @Test
    public void flux() {
        Flux.range(1, 9)
                .filter(integer -> integer % 2 == 1)
                .skip(3)
                .map(Object::toString)
                .collectList()
                .map(List.of("7", "9")::equals)
                .subscribe(Assertions::assertTrue);
    }

    @Test
    public void flat() {
        Mono.just(List.of(1, 2, 3, 4, 5))
                .flatMapMany(Flux::fromIterable)
                .skip(2)
                .take(1)
                .map(integer -> integer * integer)
                .collectList()
                .map(List.of(9)::equals)
                .subscribe(Assertions::assertTrue);
    }

    @Test
    public void reduce() {
        Flux.fromIterable(List.of(1, 2, 3, 4, 5))
                .filter(integer -> integer % 2 == 1)
                .reduce(0, Integer::sum)
                .subscribe(integer -> Assertions.assertEquals(1 + 3 + 5, integer));
    }

    @Test
    public void error() {
        // 遇到错误取消后续
        Flux.range(-2, 5)
                .map(integer -> 2 / integer)
                .onErrorReturn(0)
                .collectList()
                .map(List.of(-1, -2, 0)::equals)
                .subscribe(Assertions::assertTrue);
        // 错误回调
        List<Integer> result = new ArrayList<>();
        Flux.range(-2, 5)
                .map(integer -> 2 / integer)
                .subscribe(result::add, throwable -> Assertions.assertEquals(throwable.getClass(), ArithmeticException.class));
        Assertions.assertEquals(List.of(-1, -2), result);
    }

}
