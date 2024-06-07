package juc.printABC;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import static juc.printABC.Util.run;

public class BySemaphore {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Semaphore a = new Semaphore(1);
        Semaphore b = new Semaphore(0);
        Semaphore c = new Semaphore(0);

        run(latch, print -> {
            a.acquire();
            print.accept('A');
            b.release();
        });
        run(latch, print -> {
            b.acquire();
            print.accept('B');
            c.release();
        });
        run(latch, print -> {
            c.acquire();
            print.accept('C');
            a.release();
        });

        latch.await();
    }

}
