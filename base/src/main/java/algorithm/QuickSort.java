package algorithm;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class QuickSort {

    public static void main(String[] args) throws Exception {
        int[] arr = {8, 1, 6, 7, 5, 2, 9, 4, 3};
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(new Task(arr, 0, arr.length - 1)).get();
        System.out.println(Arrays.toString(arr));
    }

    private static class Task extends RecursiveAction {

        private int[] arr;
        private int start;
        private int end;

        public Task(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            int l = start;
            int r = end;
            boolean gt = true;
            while (l != r) {
                if (arr[l] > arr[r]) {
                    int t = arr[l];
                    arr[l] = arr[r];
                    arr[r] = t;
                    gt = !gt;
                }
                if (gt) {
                    l++;
                } else {
                    r--;
                }
            }
            Task lt = null;
            Task rt = null;
            if (start < l) {
                lt = new Task(arr, start, l - 1);
                lt.fork();
            }
            if (end > r) {
                rt = new Task(arr, r + 1, end);
                rt.fork();
            }
            if (lt != null) {
                lt.join();
            }
            if (rt != null) {
                rt.join();
            }
        }

    }

}
