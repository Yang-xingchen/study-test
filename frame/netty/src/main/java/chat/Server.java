package chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.StandardCharsets;

public class Server {

    static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        StringDecoder decoder = new StringDecoder(StandardCharsets.UTF_8);
        StringEncoder encoder = new StringEncoder(StandardCharsets.UTF_8);

        // 处理连接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 处理具体消息
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 每个连接执行一次
                            System.out.println("connect: " + ch);
                            ch.pipeline()
                                    .addLast(decoder)
                                    .addLast(encoder)
                                    .addLast(new ChatHandel());
                        }
                    });

            b.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class ChatHandel extends SimpleChannelInboundHandler<String> {

        /**
         * 记录连接
         */
        private static final ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

        /**
         * 创建连接时调用
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            GROUP.add(ctx.channel());
            ctx.writeAndFlush("connect: " + ctx.channel().id());
        }

        /**
         * 接收消息时调用
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println("read: " + msg);
            // 处理退出命令
            if ("exit".equals(msg)) {
                GROUP.remove(ctx.channel());
                ctx.close();
                return;
            }
            GROUP.writeAndFlush(ctx.channel().id() + ": " + msg, channel -> channel != ctx.channel());
        }

        /**
         * 异常时调用（异常断开连接）
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
            cause.printStackTrace();
        }
    }

}
