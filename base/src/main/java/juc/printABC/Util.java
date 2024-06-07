package juc.printABC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Util {

    private static final ConcurrentHashMap<Thread, Character> MAP = new ConcurrentHashMap<>();

    private static final AtomicReference<Character> NEXT = new AtomicReference<>('A');
    private static final Map<Character, Character> PRINT_NEXT = Map.of('A', 'B', 'B', 'C', 'C', 'A');

    static void run(CountDownLatch latch, Print consumer) {
        Consumer<Character> check = character -> {
            System.out.println(character);
            Character old = MAP.put(Thread.currentThread(), character);
            if (old != null && !old.equals(character)) {
                throw new RuntimeException("thread is print " + old + ", but print " + character);
            }
            Character expected = NEXT.getAndSet(PRINT_NEXT.get(character));
            if (!expected.equals(character)) {
                throw new RuntimeException("expected print is " + expected + ", but print " + character);
            }
        };
        new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    consumer.accept(check);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        }).start();
    }

    @FunctionalInterface
    interface Print {
        void accept(Consumer<Character> print) throws Exception;

    }

}
