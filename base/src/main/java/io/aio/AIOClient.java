package io.aio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * NIO2 网络客户端
 * 服务端: {@link AIOServer}
 */
public class AIOClient {

    private volatile boolean run = true;

    public static void main(String[] args) throws Exception {
        new AIOClient().start();
    }

    public void start() throws Exception {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        Scanner sc = new Scanner(System.in);
        // 链接服务器
        client.connect(new InetSocketAddress(AIOServer.SERVER_PORT), client, new CompletionHandler<>() {
            @Override
            public void completed(Void result, AsynchronousSocketChannel channel) {
                System.err.println("connect success");
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                // 读取消息
                channel.read(readBuffer, readBuffer, new CompletionHandler<>() {
                    @Override
                    public void completed(Integer result, ByteBuffer message) {
                        message.flip();
                        byte[] d = new byte[message.limit()];
                        message.get(d);
                        System.out.println(new String(d, UTF_8));
                        message.clear();
                        // 读取下一条消息
                        channel.read(message, message, this);
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        fail(exc);
                    }
                });
                // 写入消息
                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                writeBuffer.put(sc.nextLine().getBytes(UTF_8));
                writeBuffer.flip();
                channel.write(writeBuffer, writeBuffer, new CompletionHandler<>() {
                    @Override
                    public void completed(Integer result, ByteBuffer message) {
                        message.clear();
                        String messageStr = sc.nextLine();
                        message.put(messageStr.getBytes(UTF_8));
                        message.flip();
                        // 处理退出
                        if ("exit".equals(messageStr.toLowerCase())) {
                            channel.write(message);
                            System.err.println("exit");
                            run = false;
                            return;
                        }
                        // 写入下一条
                        channel.write(message, message, this);
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        fail(exc);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                fail(exc);
            }
        });
        while (run) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
