package com.netty.nio.group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChartServer {

    // 定义属性
    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final Integer PORT = 7062;

    // 构造器
    public GroupChartServer() {
        try {
            // 得到选择器
            selector = Selector.open();

            listenChannel = ServerSocketChannel.open();

            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));

            // 设置为非阻塞模式
            listenChannel.configureBlocking(false);

            //将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 监听事件
    public void listen() {
        try {
            // 循环处理
            while (true) {
                int count = selector.select();

                if (count > 0) {
                    // 遍历得到selectionKey集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        // 取出selectionKey
                        SelectionKey key = keyIterator.next();

                        // 监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel accept = listenChannel.accept();
                            // 设置为非阻塞
                            accept.configureBlocking(false);
                            //注册
                            accept.register(selector,SelectionKey.OP_READ);
                            // 提示
                            System.out.println(accept.getRemoteAddress() + " 上线");
                        }

                        // 监听read
                        if (key.isReadable()) {
                            // 处理读事件
                            readData(key);
                        }
                        // 删除当前key，防止重复提交
                        keyIterator.remove();
                    }
                }else {
                    System.out.println("等待连接...");
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 发生异常处理
        }
    }

    // 读取客户端信息
    private void readData(SelectionKey selectionKey) {
        // 获取关联的channel
        SocketChannel channel = null;

        try {
            // 获取channel
            channel =(SocketChannel)selectionKey.channel();

            // 创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            // 将通道数据读取到缓冲区中
            int read = channel.read(byteBuffer);

            // 判断是否读取完毕
            if (read > 0) {
                // 将缓冲区的数据转成String
                String msg = new String(byteBuffer.array());

                // 输出消息
                System.out.println("客户端："+msg);

                // 向其他客户端转发消息，排除自己
                reward(msg,channel);
            }


        }catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress()+"已离线");
                // 取消注册
                selectionKey.cancel();
                // 关闭通道
                channel.close();
            }catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // 向其他客户端转发消息
    private void reward(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        // 遍历所有注册到selector上的SocketChannel，并排除self
        for (SelectionKey key : selector.keys()) {

            // 通过key取出对应的SocketChannel
            SocketChannel targetChannel = (SocketChannel)key.channel();

            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                // 将msg储存到缓冲器中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer的数据写入通道
                targetChannel.write(buffer);
            }
        }
    }

    public static void main(String[] args) {

        // 创建服务器对象
        GroupChartServer groupChartServer = new GroupChartServer();
        groupChartServer.listen();
    }
}
