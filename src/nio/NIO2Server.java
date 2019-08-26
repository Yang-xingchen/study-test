package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

//fail
public class NIO2Server {

    final public static int SERVER_PORT = 1025;

    public static void main(String[] args) throws Exception{
        new NIO2Server().start();
        System.out.println("started");
        try{
            Thread.sleep(60*60*24);
        }catch (InterruptedException e){
        	e.printStackTrace();
        }
    }

    public void start() throws Exception{
        LinkedList<AsynchronousSocketChannel> list = new LinkedList<>();
        ExecutorService es = new ThreadPoolExecutor(8,
                8,
                1,
                TimeUnit.HOURS,
                new ArrayBlockingQueue<>(32));
        AsynchronousChannelGroup tg = AsynchronousChannelGroup.withThreadPool(es);
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(tg);
        server.bind(new InetSocketAddress(SERVER_PORT));
        server.accept(null, new CompletionHandler<>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                server.accept(attachment, this);
                try {
                    AsynchronousSocketChannel client = AsynchronousSocketChannel.open(tg);
                    client.connect(result.getRemoteAddress(), client, new CompletionHandler<>() {
                        @Override
                        public void completed(Void result0, AsynchronousSocketChannel clientChannel) {
                            System.out.println("link success");
                            list.add(clientChannel);
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            result.read(readBuffer, readBuffer, new CompletionHandler<>() {
                                @Override
                                public void completed(Integer result2, ByteBuffer buffer) {
                                    ByteBuffer read = ByteBuffer.allocate(1024);
                                    buffer.flip();
                                    read.put(buffer);
                                    result.read(buffer, buffer, this);
                                    list.stream()
                                            .filter(channel -> !channel.equals(clientChannel))
                                            .forEach(asynchronousSocketChannel -> {
                                                read.flip();
                                                asynchronousSocketChannel.write(read);
                                            });
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment){
                                    fail(exc);
                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                            fail(exc);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                fail(exc);
            }
        });
    }
}
