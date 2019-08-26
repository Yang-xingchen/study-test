package nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class NIO2Test {

    @Test
    void writeAndRead() throws IOException {
        Path path = Paths.get("nio2.txt");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.put("test".getBytes());
        byteBuffer.flip();
        fileChannel.write(byteBuffer, 0, this, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, NIO2Test attachment) {
                //method 1
                try {
                    AsynchronousFileChannel fileChannel1 = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
                    ByteBuffer buffer2 = ByteBuffer.allocate(64);
                    fileChannel1.read(buffer2, 0, buffer2, new CompletionHandler<>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            byte[] data = new byte[attachment.limit()];
                            attachment.get(data);
                            assertEquals("test", new String(data));
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            fail("read fail");
                        }
                    });
                    System.out.println("read execute");
                } catch (Exception e) {
                    fail(e);
                }
                //method 2
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(64);
                    AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
                    Future<Integer> future = channel.read(buffer, 0);
                    while (!future.isDone()){
                    }
                    buffer.flip();
                    byte[] data = new byte[buffer.limit()];
                    buffer.get(data);
                    assertEquals("test", new String(data));
                } catch (IOException e) {
                    fail(e);
                }
            }

            @Override
            public void failed(Throwable exc, NIO2Test attachment) {
                fail("write fail");
            }
        });
        System.out.println("write execute");
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
        	e.printStackTrace();
        }
    }

}
