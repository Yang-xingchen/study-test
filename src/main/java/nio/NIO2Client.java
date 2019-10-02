package nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.fail;

public class NIO2Client {

    private volatile boolean run = true;

    public static void main(String[] args) throws Exception{
        new NIO2Client().start();
    }
    public void start() throws Exception{
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        Scanner sc = new Scanner(System.in);
        client.connect(new InetSocketAddress(NIO2Server.SERVER_PORT), client, new CompletionHandler<>() {
            @Override
            public void completed(Void result, AsynchronousSocketChannel socketChannel) {
                System.err.println("connect success");
                ByteBuffer message = ByteBuffer.allocate(1024);
                message.put(sc.nextLine().getBytes(StandardCharsets.UTF_8));
                message.flip();
                socketChannel.write(message, message, new CompletionHandler<>() {
                    @Override
                    public void completed(Integer result, ByteBuffer message) {
                        message.clear();
                        String messageStr = sc.nextLine();
                        message.put(messageStr.getBytes(StandardCharsets.UTF_8));
                        message.flip();
                        if ("exit".equals(messageStr.toLowerCase())) {
                            socketChannel.write(message);
                            System.err.println("exit");
                            run = false;
                            return;
                        }
                        socketChannel.write(message, message, this);
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
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
            	e.printStackTrace();
            }
        }
    }
}
