package com.netty.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1：创建输入流
 * 2：从输入流中获取文件管道
 * 3：创建缓冲区
 * 4：将文件管道中的内容读取到缓冲中
 * 5：关闭输入流
 */
public class BasicRead {

    public static void main(String[] args) throws Exception {
        // 创建输入流
        FileInputStream fileInputStream = new FileInputStream("E:\\file1.txt");

        //从输入流中获取文件管道
        FileChannel channel = fileInputStream.getChannel();

        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将管道中的数据读取到缓冲区中
        channel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

        // 关闭流
        fileInputStream.close();
    }
}
