package juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class CompletableFutureTest {

    public static class DelayTask implements Runnable {

        private final long start;
        private final int delay;
        private final String name;

        public DelayTask(long start, int delay, String name) {
            this.start = start;
            this.delay = delay;
            this.name = name;
        }

        @Override
        public void run() {
            print(start, name + " before...");
            sleep(delay);
            print(start, name + " after...");
        }

    }

    public static class DelaySupplier implements Supplier<String> {

        private final long start;
        private final int delay;
        private final String name;

        public DelaySupplier(long start, int delay, String name) {
            this.start = start;
            this.delay = delay;
            this.name = name;
        }

        @Override
        public String get() {
            print(start, name + " before...");
            sleep(delay);
            print(start, name + " after...");
            return name;
        }

    }

    private static void print(long start, String name) {
        System.out.printf("[%2d][%33s]%s\n", (System.currentTimeMillis() - start) / 100, Thread.currentThread().getName(), name);
    }

    private static void sleep(int delay) {
        try {
            TimeUnit.MILLISECONDS.sleep(delay * 100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void base() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<Void> future = CompletableFuture.runAsync(new DelayTask(start, 1, "A"));
        print(start, "main");
        future.get();
        print(start, "complete");
    }

    @Test
    public void allOf() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        Object res = CompletableFuture.allOf(
                CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A")),
                CompletableFuture.supplyAsync(new DelaySupplier(start, 3, "B"))
        ).get();
        // null complete
        print(start, res + "complete");
    }

    @Test
    public void anyOf() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        Object res = CompletableFuture.anyOf(
                CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A")),
                CompletableFuture.supplyAsync(new DelaySupplier(start, 3, "B"))
        ).get();
        // A complete
        print(start, res + "complete");
    }

    @Test
    public void then() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A"));
        print(start, "main");
        CompletableFuture<Void> then = a.thenAccept(s -> {
            print(start, s + "then before...");
            sleep(1);
            print(start, s + "then after...");
        });
        print(start, "wait");
        then.get();
        print(start, "complete");
    }

    @Test
    public void thenAsync() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A"));
        print(start, "main");
        CompletableFuture<Void> then = a.thenAcceptAsync(s -> {
            print(start, s + "then before...");
            sleep(1);
            print(start, s + "then after...");
        });
        print(start, "wait");
        then.get();
        print(start, "complete");
    }

    @Test
    public void thenCompose() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        AtomicLong hash = new AtomicLong();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A"));
        CompletableFuture<String> b = a.thenCompose(s -> {
            print(start, s + " thenCompose before...");
            CompletableFuture<String> b1 = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "B"));
            hash.set(b1.hashCode());
            sleep(1);
            print(start, s + " thenCompose after...");
            return b1;
        });
        print(start, "wait");
        print(start, b.get() + " complete");
        System.out.println("hash: " + (hash.get() == b.hashCode()));
    }

    //// thenAcceptBoth: 完成两者，并以两者为参数执行方法

    @Test
    public void thenAcceptBoth() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "B"));
        CompletableFuture<Void> runAfterBoth = a.thenAcceptBoth(b, (a1, b1) -> {
            print(start, a1 + " " + b1 + " runAfterBoth before...");
            sleep(1);
            print(start, a1 + " " + b1 + " runAfterBoth after...");
        });
        print(start, "wait");
        runAfterBoth.get();
        print(start, "runAfter");
    }

    @Test
    public void thenAcceptBoth2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "B"));
        CompletableFuture<Void> runAfterBoth = a.thenAcceptBoth(b, (a1, b1) -> {
            print(start, a1 + " " + b1 + " runAfterBoth before...");
            sleep(1);
            print(start, a1 + " " + b1 + " runAfterBoth after...");
        });
        print(start, "wait");
        runAfterBoth.get();
        print(start, "runAfter");
    }

    //// runAfterEither: 完成两者，然后执行方法产生新者

    @Test
    public void thenCombine() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "B"));
        CompletableFuture<String> thenCombine = a.thenCombine(b, (a1, b1) -> {
            print(start, a1 + " " + b1 + " thenCombine before...");
            sleep(1);
            print(start, a1 + " " + b1 + " thenCombine after...");
            return a1 + " " + b1;
        });
        print(start, "wait");
        print(start, thenCombine.get() + " runAfter");
    }

    @Test
    public void thenCombine2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "B"));
        CompletableFuture<String> thenCombine = a.thenCombine(b, (a1, b1) -> {
            print(start, a1 + " " + b1 + " thenCombine before...");
            sleep(1);
            print(start, a1 + " " + b1 + " thenCombine after...");
            return a1 + " " + b1;
        });
        print(start, "wait");
        print(start, thenCombine.get() + " runAfter");
    }

    //// runAfterEither: 完成两者，然后执行方法

    /**
     * [ 0][ ForkJoinPool.commonPool-worker-1]A before...
     * [ 0][ ForkJoinPool.commonPool-worker-3]B before...
     * [ 0][                             main]wait
     * [ 1][ ForkJoinPool.commonPool-worker-1]A after...
     * [ 2][ ForkJoinPool.commonPool-worker-3]B after...                // ???
     * [ 3][ ForkJoinPool.commonPool-worker-2]B after...
     * [ 2][ ForkJoinPool.commonPool-worker-3]runAfterBoth before...
     * [ 3][ ForkJoinPool.commonPool-worker-3]runAfterBoth after...
     * [ 3][                             main]runAfter
     */
    @Test
    public void runAfterBoth() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "B"));
        CompletableFuture<Void> runAfterBoth = a.runAfterBoth(b, () -> {
            print(start, "runAfterBoth before...");
            sleep(1);
            print(start, "runAfterBoth after...");

        });
        print(start, "wait");
        runAfterBoth.get();
        print(start, "runAfter");
    }

    @Test
    public void runAfterBoth2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "B"));
        CompletableFuture<Void> runAfterBoth = a.runAfterBoth(b, () -> {
            print(start, "runAfterBoth before...");
            sleep(1);
            print(start, "runAfterBoth after...");

        });
        print(start, "wait");
        runAfterBoth.get();
        print(start, "runAfter");
    }

    //// runAfterEither: 完成一者，然后执行方法

    @Test
    public void runAfterEither() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "B"));
        CompletableFuture<Void> runAfterEither = a.runAfterEither(b, () -> {
            print(start, "runAfterEither before...");
            sleep(1);
            print(start, "runAfterEither after...");
        });
        print(start, "wait");
        runAfterEither.get();
        print(start, "runAfter");
    }

    @Test
    public void runAfterEither2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "B"));
        CompletableFuture<Void> runAfterEither = a.runAfterEither(b, () -> {
            print(start, "runAfterEither before...");
            sleep(1);
            print(start, "runAfterEither after...");
        });
        print(start, "wait");
        runAfterEither.get();
        print(start, "runAfter");
    }

    //// applyToEither: 完成一者，并以其中一者为参数执行方法

    @Test
    public void applyToEither() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "B"));
        CompletableFuture<String> runAfterEither = a.applyToEither(b, s -> {
            print(start, s + " applyToEither before...");
            sleep(1);
            print(start, s + " applyToEither after...");
            return s;
        });
        print(start, "wait");
        print(start, runAfterEither.get() + " runAfter");
        sleep(5);
    }

    @Test
    public void applyToEither2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> a = CompletableFuture.supplyAsync(new DelaySupplier(start, 2, "A"));
        CompletableFuture<String> b = CompletableFuture.supplyAsync(new DelaySupplier(start, 1, "B"));
        CompletableFuture<String> runAfterEither = a.applyToEither(b, s -> {
            print(start, s + " applyToEither before...");
            sleep(1);
            print(start, s + " applyToEither after...");
            return s;
        });
        print(start, "wait");
        print(start, runAfterEither.get() + " runAfter");
        sleep(5);
    }


}
