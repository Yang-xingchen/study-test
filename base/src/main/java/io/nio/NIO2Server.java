package io.nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * NIO2网络服务端
 * 客户端: {@link NIO2Client}
 */
public class NIO2Server {

    final static int SERVER_PORT = 1025;

    private CopyOnWriteArrayList<AsynchronousSocketChannel> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws Exception {
        new NIO2Server().start();
        System.out.println("started");
        try {
            Thread.sleep(60 * 60 * 24);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        ExecutorService es = new ThreadPoolExecutor(8,
                8,
                1,
                TimeUnit.HOURS,
                new ArrayBlockingQueue<>(32));
        AsynchronousChannelGroup tg = AsynchronousChannelGroup.withThreadPool(es);
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(tg);
        // 绑定端口
        server.bind(new InetSocketAddress(SERVER_PORT));
        // 接收链接
        server.accept(server, new CompletionHandler<>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel server) {
                // 下一条链接
                server.accept(server, this);
                clients.add(channel);
                System.out.println("connect success");
                // 分配空间
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                // 读取内容
                channel.read(readBuffer, readBuffer, new CompletionHandler<>() {
                    @Override
                    public void completed(Integer result, ByteBuffer message) {
                        message.flip();
                        writeBuffer.put(message);
                        message.flip();
                        byte[] d = new byte[message.limit()];
                        message.get(d);
                        String messageStr = new String(d, StandardCharsets.UTF_8);
                        // 处理退出指令
                        if ("exit".equals(messageStr.toLowerCase())) {
                            System.err.println("client exit");
                            return;
                        }
                        System.out.println(messageStr);
                        // 分发消息
                        clients.stream()
                                .filter(c -> !c.equals(channel))
                                .forEach(c -> c.write(writeBuffer.flip()));
                        writeBuffer.clear();
                        // 接收处理下一条消息
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
