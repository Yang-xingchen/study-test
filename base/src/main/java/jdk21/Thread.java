package jdk21;

public class Thread {

    public static void main(String[] args) throws InterruptedException {
//        long start = System.currentTimeMillis();
//        CountDownLatch latch = new CountDownLatch(10_000);
//        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
//            for (int i = 0; i < 10_000; i++) {
//                executor.submit(() -> {
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } finally {
//                        latch.countDown();
//                    }
//                });
//            }
//        }
//        latch.await();
//        System.out.println(System.currentTimeMillis() - start);
    }

}
