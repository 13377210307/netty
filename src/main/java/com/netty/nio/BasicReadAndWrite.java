package com.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用write以及read实现一个文件拷贝到另一个文件
 *
 * 1：创建文件输入、输出流
 * 2：从输入流、输出流中获取文件管道
 * 3：创建缓冲区
 * 4：进行循环读取，只有当缓冲区的内容为空时不再进行读取（文件管道读取缓冲区）
 * 5：缓冲区进行反转
 * 6：缓冲区数据写入文件管道
 */
public class BasicReadAndWrite {

    public static void main(String[] args) throws Exception{
        // 创建输入流
        FileInputStream fileInputStream = new FileInputStream("E:\\file1.txt");

        // 从输入流中获取文件管道
        FileChannel inputChannel = fileInputStream.getChannel();

        // 创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\file2.txt");

        // 从输出流中获取文件管道
        FileChannel outputChannel = fileOutputStream.getChannel();

        // 创建缓冲流
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);  // 表示每次读取或写入的size，当size小于文件内容时就需要进行多次读写，所以需要循环判断以及判断文件是否读取完了

        // 循环读取
        while (true) {
            // 清空缓冲区
            byteBuffer.clear();

            // 读取文件管道数据
            int read = inputChannel.read(byteBuffer);

            // 判断是否读取完
            if (read == -1) {
                break;
            }
            // 缓冲流反转
            byteBuffer.flip();

            // 将缓冲区中的数据写入到文件管道中
            outputChannel.write(byteBuffer);

        }


        // 关闭输入、输出流
        fileInputStream.close();
        fileOutputStream.close();

    }

}
