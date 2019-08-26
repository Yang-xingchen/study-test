package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.fail;

//fail
public class NIO2Client {
    public static void main(String[] args) throws Exception{
        new NIO2Client().start();
        try{
            Thread.sleep(24*60*60);
        }catch (InterruptedException e){
        	e.printStackTrace();
        }
    }
    public void start() throws Exception{
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        Scanner sc = new Scanner(System.in);
        client.connect(new InetSocketAddress(NIO2Server.SERVER_PORT), client, new CompletionHandler<>() {
            @Override
            public void completed(Void result, AsynchronousSocketChannel attachment) {
                try {
                    AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
                    server.bind(attachment.getRemoteAddress());
                    server.accept(null, new CompletionHandler<>() {
                        @Override
                        public void completed(AsynchronousSocketChannel result, Object attachment) {
                            System.out.println("connect success");
                            ByteBuffer sendMessage = ByteBuffer.allocate(64);
                            sendMessage.put(sc.nextLine().getBytes());
                            sendMessage.flip();
                            client.write(sendMessage, sendMessage, new CompletionHandler<>() {
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    System.out.println("send success");
                                    sendMessage.clear();
                                    sendMessage.put(sc.nextLine().getBytes());
                                    sendMessage.flip();
                                    client.write(sendMessage, sendMessage, this);
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    fail("send fail");
                                }
                            });

                            ByteBuffer receiveMessage = ByteBuffer.allocate(1024);
                            result.read(receiveMessage, receiveMessage, new CompletionHandler<>() {
                                @Override
                                public void completed(Integer result0, ByteBuffer attachment) {
                                    attachment.flip();
                                    byte[] data = new byte[attachment.limit()];
                                    attachment.put(data);
                                    attachment.clear();
                                    result.read(receiveMessage, receiveMessage, this);
                                    System.out.println("receive message:" + new String(data));
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    fail("receive fail");
                                }
                            });
                        }
                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            fail(exc);
                        }
                    });
                }catch (IOException e){
                    fail(e);
                }
            }
            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                fail(exc);
            }
        });

    }
}
