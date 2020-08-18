package com.netty.netty.group;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChartClient1 {

    // 定义属性
    private final String host;

    private final int port;

    public GroupChartClient1(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取pipeline
                            ChannelPipeline channelPipeline = socketChannel.pipeline();

                            // 加入编码、解码器
                            channelPipeline.addLast("encoder",new StringEncoder());
                            channelPipeline.addLast("decoder",new StringDecoder());

                            // 加入自定义handler
                            channelPipeline.addLast(new GroupChartClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port);

            // 获取channel
            Channel channel = channelFuture.channel();
            System.out.println("------------"+channel.remoteAddress()+"--------------");

            //客户端输入信息
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                // 将信息写入并刷新
                channel.writeAndFlush(msg+"\r\n");
            }
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new GroupChartClient1("127.0.0.1",7000).run();
    }


}
