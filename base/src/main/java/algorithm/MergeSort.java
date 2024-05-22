package algorithm;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSort {

    public static void main(String[] args) throws Exception {
        int[] arr = {8, 1, 6, 7, 5, 2, 9, 4, 3};
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(new Task(arr)).get();
        System.out.println(Arrays.toString(arr));
    }

    private static class Task extends RecursiveAction {

        private int[] arr;
        private int[] temp;
        private int start;
        private int end;

        public Task(int[] arr) {
            this.arr = arr;
            this.temp = new int[arr.length];
            this.start = 0;
            this.end = arr.length - 1;
        }

        private Task(int[] arr, int[] temp, int start, int end) {
            this.arr = arr;
            this.temp = temp;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (start == end) {
                return;
            }
            if (start + 1 == end) {
                if (arr[start] > arr[end]) {
                    int t = arr[start];
                    arr[start] = arr[end];
                    arr[end] = t;
                }
                return;
            }
            int mid = (start + end) / 2;
            Task lt = null;
            if (mid - 1 > start) {
                lt = new Task(arr, temp, start, mid - 1);
                lt.fork();
            }
            Task rt = null;
            if (end > mid) {
                rt = new Task(arr, temp, mid, end);
                rt.fork();
            }
            if (lt != null) {
                lt.join();
            }
            if (rt != null) {
                rt.join();
            }
            System.arraycopy(arr, start, temp, start, end - start + 1);
            int p = start;
            int lp = start;
            int rp = mid;
            while (p <= end) {
                if (lp == mid) {
                    arr[p++] = temp[rp++];
                } else if (rp == end + 1) {
                    arr[p++] = temp[lp++];
                } else {
                    if (temp[lp] > temp[rp]) {
                        arr[p++] = temp[rp++];
                    } else {
                        arr[p++] = temp[lp++];
                    }
                }
            }
        }

    }

}
