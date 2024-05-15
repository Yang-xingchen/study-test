package generator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 参考: https://mp.weixin.qq.com/s/v-HMKBWxtz1iakxFL09PDw
 */
public class Main {

    /**
     * a
     * b
     * c
     */
    @Test
    public void base() {
        List<String> list = List.of("a", "b", "c");
        Seq<String> seq = list::forEach;
        seq.consumer(System.out::println);
    }

    /**
     *
     */
    @Test
    public void noEnd() {
        List<String> list = List.of("a", "b", "c");
        Seq<String> seq = list::forEach;
        seq.onEach(System.out::println);
    }

    /**
     * aa
     * bb
     * cc
     */
    @Test
    public void map() {
        List<String> list = List.of("a", "b", "c");
        Seq<String> seq = list::forEach;
        seq.map(s -> s + s).consumer(System.out::println);
    }

    /**
     * a
     * a
     * b
     * b
     * c
     * c
     */
    @Test
    public void flatMap() {
        List<String> list = List.of("a", "b", "c");
        Seq<String> seq = list::forEach;
        seq.flatMap(s -> List.of(s, s)::forEach).consumer(System.out::println);
    }

    /**
     * 2
     * 4
     */
    @Test
    public void filter() {
        List<Integer> list = List.of(1, 2, 3, 4);
        Seq<Integer> seq = list::forEach;
        seq.filter(i -> i % 2 == 0).consumer(System.out::println);
    }

    /**
     * 1
     * 2
     */
    @Test
    public void take() {
        Seq<Integer> seq = c -> {
            int i = 1;
            while (true) {
                c.accept(i++);
            }
        };
        seq.take(2).consumer(System.out::println);
    }

    /**
     * 3
     * 4
     */
    @Test
    public void drop() {
        List<Integer> list = List.of(1, 2, 3, 4);
        Seq<Integer> seq = list::forEach;
        seq.drop(2).consumer(System.out::println);
    }

    /**
     * 1
     * 2
     * 3
     * 4
     * [1, 2, 3, 4]
     */
    @Test
    public void onEach() {
        List<Integer> list = List.of(1, 2, 3, 4);
        Seq<Integer> seq = list::forEach;
        List<Integer> list1 = seq.onEach(System.out::println).toList();
        System.out.println(list1);
    }

    /**
     * [1, 4, 9, 16]
     */
    @Test
    public void toList() {
        List<Integer> list = List.of(1, 2, 3, 4);
        Seq<Integer> seq = list::forEach;
        List<Integer> list1 = seq.map(i -> i * i).toList();
        System.out.println(list1);
    }

    /**
     * 1
     * 2
     * 3
     * 4
     */
    @Test
    public void distinct() {
        List<Integer> list = List.of(1, 2, 2, 3, 4);
        Seq<Integer> seq = list::forEach;
        seq.distinct().consumer(System.out::println);
    }

    /**
     * [1, 2, 2, 3, 4]
     */
    @Test
    public void collect() {
        List<Integer> list = List.of(1, 2, 2, 3, 4);
        Seq<Integer> seq = list::forEach;
        List<Integer> list1 = seq.collect(Collectors.toList());
        System.out.println(list1);
    }

    /**
     * [1, 4, 9, 16]
     */
    @Test
    public void toStream() {
        List<Integer> list = List.of(1, 2, 3, 4);
        Seq<Integer> seq = list::forEach;
        List<Integer> list1 = seq.map(i -> i * i).toStream().toList();
        System.out.println(list1);
    }

    /**
     * 1
     * 2
     * 3
     * 4
     * ======
     * 1
     * 2
     * 3
     * ======
     * 1
     * 2
     * 3
     */
    @Test
    public void fromStream() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4);
        Seq.fromStream(stream).consumer(System.out::println);
        System.out.println("======");
        Stream<Integer> iterate = Stream.iterate(1, i -> i + 1);
        iterate.limit(3).forEach(System.out::println);
        System.out.println("======");
        iterate = Stream.iterate(1, i -> i + 1);
        Seq.fromStream(iterate).take(3).consumer(System.out::println);
    }

    /**
     * a1
     * b2
     * c3
     */
    @Test
    public void zip() {
        List<Integer> list = List.of(1, 2, 3, 4);
        Seq<Integer> seq = list::forEach;
        List<String> list2 = List.of("a", "b", "c");
        seq.zip(list2, (i, s) -> s + i).consumer(System.out::println);
    }

    /**
     * oneTwoThreeFour
     */
    @Test
    public void underscoreToCamel() {
        String src = "one_two_three_four";
        String res = ((Seq<Function<String, String>>) c -> {
            c.accept(Function.identity());
            while (true) {
                c.accept(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
            }
        }).zip(Arrays.stream(src.split("_")).toList(), Function::apply).joining();
        System.out.println(res);
    }

    /**
     * 0
     * 5
     * a: 5
     * b: 6
     */
    @Test
    public void sleep() {
        long base = System.currentTimeMillis();
        System.out.println(0);
        Seq<String> seq = c -> {
            c.accept("a");
            sleep(100);
            c.accept("b");
            sleep(100);
        };
        sleep(500);
        System.out.println((System.currentTimeMillis() - base) / 100);
        seq.consumer(s -> System.out.println(s + ": " + ((System.currentTimeMillis() - base) / 100)));
    }

    private void sleep(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (Exception ignore) {
        }
    }

}
