package com.netty.netty.one;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) throws Exception {
        // 客户端需要一个循环事件组
        EventLoopGroup eventGroup = new NioEventLoopGroup();

        try {
            // 创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();

            // 设置相关参数
            bootstrap.group(eventGroup)   // 设置线程组
                    .channel(NioSocketChannel.class)  // 设置客户端通道的实现类（反射）
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端启动成功");

            // 连接服务器端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",6668).sync();

            // 设置监听关闭通道
            channelFuture.channel().closeFuture().sync();

        }finally {
            eventGroup.shutdownGracefully();
        }
    }
}
