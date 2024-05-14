package generator;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FunctionalInterface
public interface Seq<T> {

    void consumer(Consumer<T> consumer);

    /**
     * {@link Stream#map(Function)}
     */
    default <R> Seq<R> map(Function<T, R> function) {
        return c -> consumer(t -> c.accept(function.apply(t)));
    }

    /**
     * {@link Stream#flatMap(Function)}
     */
    default <R> Seq<R> flatMap(Function<T, Seq<R>> function) {
        return c -> consumer(t -> function.apply(t).consumer(c));
    }

    /**
     * {@link Stream#filter(Predicate)}
     */
    default Seq<T> filter(Predicate<T> predicate) {
        return c -> consumer(t -> {
            if (predicate.test(t)) {
                c.accept(t);
            }
        });
    }

    default void consumerTillStop(Consumer<T> consumer) {
        try {
            consumer(consumer);
        } catch (StopException ignore) {
        }
    }

    /**
     * {@link Stream#limit(long)}
     */
    default Seq<T> take(long n) {
        return c -> {
            AtomicLong i = new AtomicLong();
            consumerTillStop(t -> {
                if (i.incrementAndGet() > n) {
                    throw StopException.INSTANCE;
                }
                c.accept(t);
            });
        };
    }

    /**
     * {@link Stream#skip(long)}
     */
    default Seq<T> drop(long n) {
        return c -> {
            AtomicLong i = new AtomicLong();
            consumerTillStop(t -> {
                if (i.incrementAndGet() > n) {
                    c.accept(t);
                }
            });
        };
    }

    /**
     * {@link Stream#peek(Consumer)}
     */
    default Seq<T> onEach(Consumer<T> consumer) {
        return c -> consumer(t -> {
            consumer.accept(t);
            c.accept(t);
        });
    }

    default List<T> toList() {
        List<T> list = new ArrayList<>();
        consumer(list::add);
        return list;
    }

    default Set<T> toSet() {
        Set<T> set = new HashSet<>();
        consumer(set::add);
        return set;
    }

    /**
     * {@link Stream#distinct()}
     */
    default Seq<T> distinct() {
        return toSet()::forEach;
    }

    /**
     * {@link Collectors#joining()}
     */
    default String joining() {
        StringBuilder stringBuilder = new StringBuilder();
        consumer(stringBuilder::append);
        return stringBuilder.toString();
    }

    /**
     * {@link Stream#collect(Collector)}
     */
    default <A, R> R collect(Collector<T, A, R> collector) {
        A a = collector.supplier().get();
        consumer(t -> collector.accumulator().accept(a, t));
        return collector.finisher().apply(a);
    }

    default Stream<T> toStream() {
        Stream.Builder<T> builder = Stream.builder();
        consumer(builder);
        return builder.build();
    }

    static <T> Seq<T> fromStream(Stream<T> stream) {
        return stream::forEach;
    }

    default <E, R> Seq<R> zip(Iterable<E> iterable, BiFunction<T, E, R> function) {
        return c -> {
            Iterator<E> iterator = iterable.iterator();
            consumerTillStop(t -> {
                if (iterator.hasNext()) {
                    c.accept(function.apply(t, iterator.next()));
                } else {
                    throw StopException.INSTANCE;
                }
            });
        };
    }

}
