package com.netty.nio.nioDemo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient1 {

    public static void main(String[] args) throws Exception {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        // 设置为非阻塞
        socketChannel.configureBlocking(false);

        // 提供服务器端口ip以及端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",7061);

        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {

            while (!socketChannel.finishConnect()) {
                System.out.println("客户端连接需要5秒时间");
            }
        }

        // 连接成功，发送数据
        String str = "我感觉很棒";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据。将buffer写入管道
        socketChannel.write(byteBuffer);

        System.in.read();

    }
}
