package com.netty.nio.group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChartClient {

    // 定义属性
    private Selector selector;

    private SocketChannel socketChannel;

    private final String HOST = "127.0.0.1";

    private static final Integer PORT = 7062;

    private String username;

    // 构造器，完成初始化工作
    public GroupChartClient() throws IOException {
        selector = Selector.open();

        // 连接服务器
        socketChannel = socketChannel.open(new InetSocketAddress(HOST,PORT));

        // 设置非阻塞
        socketChannel.configureBlocking(false);

        //将channel注册到selector中
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 获取username
        username = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username + "is ok...");
    }

    // 向服务器发送消息
    public void sendMessage(String info) {
        info = username + "："+info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取从服务器端辉副的消息
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                // 有可用通道
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isReadable()) {
                        // 得到相关通道
                        SocketChannel channel = (SocketChannel)key.channel();

                        // 得到一个缓冲区
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                        // 读取
                        channel.read(byteBuffer);

                        // 将读取到缓冲区的数据转成字符串
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());
                    }
                }
                // 移除selectionKey
                keyIterator.remove();
            }else {

            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        // 启动客户端
        GroupChartClient groupChartClient = new GroupChartClient();

        // 启动线程
        new Thread() {
            public void run() {
                while (true) {
                    groupChartClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            groupChartClient.sendMessage(s);
        }
        //groupChartClient.sendMessage("你好");
    }
}
