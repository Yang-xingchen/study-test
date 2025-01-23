package io.bio;

import org.junit.jupiter.api.Assertions;

import java.io.BufferedReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PipeeTest {

    public static void main(String[] args) throws Exception {
        // 类似Queue，但分离生产者和消费者使用不同对象
        try (PipedWriter writer = new PipedWriter();
             PipedReader reader = new PipedReader()) {
            // 链接
            writer.connect(reader);
            CountDownLatch latch = new CountDownLatch(2);
            Thread.ofVirtual().name("writer").start(() -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        writer.write("write: " + i + "\n");
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
            Thread.ofVirtual().name("reader").start(() -> {
                try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                    for (int i = 0; i < 5; i++) {
                        Assertions.assertEquals("write: " + i, bufferedReader.readLine());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
            latch.await();
        }
    }
}
