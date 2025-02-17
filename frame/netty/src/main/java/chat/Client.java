package chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        StringDecoder decoder = new StringDecoder(StandardCharsets.UTF_8);
        StringEncoder encoder = new StringEncoder(StandardCharsets.UTF_8);

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(decoder)
                                    .addLast(encoder)
                                    .addLast(new ChatHandel());
                        }
                    });
            Channel ch = b.connect("127.0.0.1", Server.PORT).sync().channel();

            // 接收控制台输入数据
            Scanner sc = new Scanner(System.in);
            ChannelFuture lastWriteFuture = null;
            while (true) {
                String line = sc.nextLine();
                if (line == null) {
                    break;
                }
                lastWriteFuture = ch.writeAndFlush(line);

                if ("exit".equals(line)) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public static class ChatHandel extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
            cause.printStackTrace();
        }

    }

}
