package com.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel {

    public static void main(String[] args) throws Exception {
        // 定义输入内容
        String str = "hello,netty";

        // 创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\file01.txt");

        // 通过FileOutputStream获取FileChannel
        FileChannel channel = fileOutputStream.getChannel();

        // 创建缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);

        //将str放入缓冲区
        allocate.put(str.getBytes());

        // 对byteBuffer进行flip
        allocate.flip();

        // 将byteBuffer数据写入fileChannel
        channel.write(allocate);

        // 关闭流
        fileOutputStream.close();
    }
}
