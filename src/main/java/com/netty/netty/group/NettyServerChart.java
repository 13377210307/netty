package com.netty.netty.group;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServerChart {

    // 定义属性
    private int port;  // 监听端口

    // 构造方法
    public NettyServerChart(int port) {
        this.port = port;
    }

    // 编写run方法，处理客户端请求
    public void run() throws Exception {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取pipeline
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            // 向pipeline加入解码器
                            channelPipeline.addLast("decoder",new StringDecoder());
                            // 向pipeLine加入编码器
                            channelPipeline.addLast("encoder",new StringEncoder());
                            // 加入自己的业务处理handler
                            channelPipeline.addLast(new GroupChartServerHandler());
                        }
                    });
            System.out.println("服务器启动了");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 监听关闭
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        new NettyServerChart(7000).run();
    }
}
