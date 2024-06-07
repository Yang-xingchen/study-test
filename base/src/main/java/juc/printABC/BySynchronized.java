package juc.printABC;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static juc.printABC.Util.run;

public class BySynchronized {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Object lock = new Object();
        AtomicInteger point = new AtomicInteger();

        run(latch, print -> {
            synchronized (lock) {
                while (point.get() % 3 != 0) {
                    lock.wait();
                }
                point.incrementAndGet();
                print.accept('A');
                lock.notifyAll();
            }
        });
        run(latch, print -> {
            synchronized (lock) {
                while (point.get() % 3 != 1) {
                    lock.wait();
                }
                point.incrementAndGet();
                print.accept('B');
                lock.notifyAll();
            }
        });
        run(latch, print -> {
            synchronized (lock) {
                while (point.get() % 3 != 2) {
                    lock.wait();
                }
                point.incrementAndGet();
                print.accept('C');
                lock.notifyAll();
            }
        });

        latch.await();
    }

}
