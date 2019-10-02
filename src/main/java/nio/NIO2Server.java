package nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

public class NIO2Server {

    final static int SERVER_PORT = 1025;

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
        server.accept(server, new CompletionHandler<>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel server) {
                server.accept(server, this);
                System.err.println("connect success");
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                channel.read(byteBuffer, byteBuffer, new CompletionHandler<>() {
                    @Override
                    public void completed(Integer result, ByteBuffer message) {
                        message.flip();
                        byte[] d = new byte[message.limit()];
                        message.get(d);
                        String messageStr = new String(d, StandardCharsets.UTF_8);
                        if ("exit".equals(messageStr.toLowerCase())) {
                            System.err.println("client exit");
                            return;
                        }
                        System.out.println(messageStr);
                        message.clear();
                        channel.read(message, message, this);
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        fail(exc);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                fail(exc);
            }
        });
    }
}
