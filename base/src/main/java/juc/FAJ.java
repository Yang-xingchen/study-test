package juc;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class FAJ {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        int[] array = IntStream.range(1, 10001).toArray();
        ForkJoinTask<Long> submit = forkJoinPool.submit(new Sum(array));
        int sum = Arrays.stream(array).sum();
        assert sum == submit.get();
    }


    static class Sum extends RecursiveTask<Long> {

        int[] arr;
        int start;
        int end;

        public Sum(int[] arr) {
            this.arr = arr;
            start = 0;
            end = arr.length - 1;
        }

        public Sum(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (start == end) {
                return (long)arr[start];
            }
            if (end - start == 1) {
                return (long)arr[start] + arr[end];
            }
            int mid = (start + end) / 2;
            Sum l = new Sum(arr, start, mid);
            Sum r = new Sum(arr, mid + 1, end);
            l.fork();
            r.fork();
            long li = l.join();
            long ri = r.join();
            return l.join() + r.join();
        }
    }
}
