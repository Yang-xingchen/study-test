package juc.printABC;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static juc.printABC.Util.run;

public class ByLockCondition {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Lock lock = new ReentrantLock();
        Condition a = lock.newCondition();
        Condition b = lock.newCondition();
        Condition c = lock.newCondition();

        run(latch, print -> {
            lock.lock();
            try {
                a.await();
                print.accept('A');
                b.signal();
            } finally {
                lock.unlock();
            }
        });
        run(latch, print -> {
            lock.lock();
            try {
                b.await();
                print.accept('B');
                c.signal();
            } finally {
                lock.unlock();
            }
        });
        run(latch, print -> {
            lock.lock();
            try {
                c.await();
                print.accept('C');
                a.signal();
            } finally {
                lock.unlock();
            }
        });

        // start
        lock.lock();
        try {
            a.signal();
        } finally {
            lock.unlock();
        }

        latch.await();
    }

}
