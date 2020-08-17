package com.netty.nio.nioDemo;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws Exception {

        // 创建ServerSocketChannel-->ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个selector对象
        Selector selector = Selector.open();

        // 绑定一个端口
        serverSocketChannel.socket().bind(new InetSocketAddress(7061));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 将serverSocketChannel注册到selector关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {

            // 等待一秒，没有事件发生就返回
            if (selector.select(5000) == 0) {
                System.out.println("服务器无响应，连接失败");
                continue;
            }

            // 通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历selectionKeys
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                // 获取selectionKey
                SelectionKey selectionKey = keyIterator.next();
                // 根据key对应的通道发生的事件做相应处理
                if (selectionKey.isAcceptable()) {  // 表示有新客户端连接成功
                    // 该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    System.out.println("有新的客户端连接成功");

                    // 将SocketChannel设为非阻塞
                    socketChannel.configureBlocking(false);

                    //将serverSocketChannel注册到selector关心事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) {  // 可读
                    handlerRead(selectionKey);
                }

                // 手动移除集合中的selectKey,防止重复操作
                keyIterator.remove();
            }
        }
    }

    /**
     * 读操作
     */
    private static void handlerRead(SelectionKey selectionKey) throws IOException {
        // 获取事件发生的Socket通道
        SocketChannel readSocketChannel = (SocketChannel)selectionKey.channel();
        // 创建可读缓冲区
        ByteBuffer readByteBuffer = (ByteBuffer)selectionKey.attachment();

        // 通道读取缓冲区数据
        int read = readSocketChannel.read(readByteBuffer);

        if (read > 0) {
            System.out.println("客户端发送消息为："+new String(readByteBuffer.array()));
            // 回写数据， 将消息回送给客户端
            ByteBuffer outBuffer = ByteBuffer.wrap("好的".getBytes());
            readSocketChannel.write(outBuffer);
        }else {
            System.out.println("客户端关闭");
            selectionKey.cancel();
        }
    }
}
