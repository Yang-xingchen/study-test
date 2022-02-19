package juc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

public class Accumulator {

    /**
     * slow
     */
    @Test
    public void atomic() throws InterruptedException {
        AtomicLong accumulator = new AtomicLong();
        int threadSize = Runtime.getRuntime().availableProcessors() * 4;
        CountDownLatch latch = new CountDownLatch(threadSize);
        List<Thread> threadList = IntStream.range(0, threadSize).mapToObj(i -> new Thread(() -> {
            for (int j = 0; j < 1_000_000; j++) {
                accumulator.incrementAndGet();
            }
            latch.countDown();
        }, "t-" + i)).toList();
        long start = System.currentTimeMillis();
        threadList.forEach(Thread::start);
        latch.await();
        System.out.println(System.currentTimeMillis() - start);
        Assertions.assertEquals(threadSize * 1_000_000L, accumulator.get());
    }

    /**
     * fast
     */
    @Test
    public void accumulator() throws InterruptedException {
        LongAccumulator accumulator = new LongAccumulator(Long::sum, 0);
        int threadSize = Runtime.getRuntime().availableProcessors() * 4;
        CountDownLatch latch = new CountDownLatch(threadSize);
        List<Thread> threadList = IntStream.range(0, threadSize).mapToObj(i -> new Thread(() -> {
            for (int j = 0; j < 1_000_000; j++) {
                accumulator.accumulate(1);
            }
            latch.countDown();
        }, "t-" + i)).toList();
        long start = System.currentTimeMillis();
        threadList.forEach(Thread::start);
        latch.await();
        System.out.println(System.currentTimeMillis() - start);
        Assertions.assertEquals(threadSize * 1_000_000L, accumulator.get());
    }

}
