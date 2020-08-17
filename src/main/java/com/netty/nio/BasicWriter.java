package com.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1：创建输出流
 * 2：从输出流中获取文件管道
 * 3：创建缓冲区
 * 4：将输出内容放入缓冲区
 * 5：缓冲区进行反转
 * 6：将缓冲区数据写入文件管道
 */
public class BasicWriter {

    public static void main(String[] args) throws Exception {
        // 定义输出内容
        String str = "学习netty";

        // 创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\file1.txt");

        // 获取文件管道
        FileChannel channel = fileOutputStream.getChannel();

        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将输出内容放入缓冲区
        byteBuffer.put(str.getBytes());

        // 缓冲区进行反转
        byteBuffer.flip();

        // 将缓冲区中数据写入到文件管道
        channel.write(byteBuffer);

        // 关闭输出流
        fileOutputStream.close();

    }
}
