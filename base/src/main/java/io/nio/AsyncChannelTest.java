package io.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AsyncChannelTest {

    @Test
    void writeAndReadByFuture() throws IOException {
        Path path = Paths.get("nio2.txt");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.put("test".getBytes());
        byteBuffer.flip();
        Future<Integer> write = fileChannel.write(byteBuffer, 0);
        // do something...
        while (!write.isDone()) {
        }
        try {
            ByteBuffer buffer = ByteBuffer.allocate(64);
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
            Future<Integer> read = channel.read(buffer, 0);
            // do something...
            while (!read.isDone()) {
            }
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            assertEquals("test", new String(data));
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void writeAndReadByCallback() throws IOException, InterruptedException {
        Path path = Paths.get("nio2.txt");
        AsynchronousFileChannel writeChannel = AsynchronousFileChannel.open(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);
        ByteBuffer writeBuffer = ByteBuffer.allocate(64);
        writeBuffer.put("test".getBytes());
        writeBuffer.flip();
        // 写入
        writeChannel.write(writeBuffer, 0, path, new CompletionHandler<>() {
            /**
             * 写入完成回调
             */
            @Override
            public void completed(Integer result, Path path) {
                System.out.println("write completed");
                // method 1
                try {
                    AsynchronousFileChannel readChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
                    ByteBuffer readBuffer = ByteBuffer.allocate(64);
                    readChannel.read(readBuffer, 0, readBuffer, new CompletionHandler<>() {
                        /**
                         * 读取完成回调
                         */
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            System.out.println("read completed");
                            buffer.flip();
                            byte[] data = new byte[buffer.limit()];
                            buffer.get(data);
                            assertEquals("test", new String(data));
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            fail("read fail");
                        }
                    });
                    // do something...
                    System.out.println("read execute");
                } catch (Exception e) {
                    fail(e);
                }
            }

            @Override
            public void failed(Throwable exc, Path path) {
                fail("write fail");
            }
        });
        System.out.println("write execute");
        // do something...
        TimeUnit.SECONDS.sleep(5);
    }

}
