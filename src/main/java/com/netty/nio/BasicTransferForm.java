package com.netty.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 使用transferForm将一个文件拷贝到另一个文件
 */
public class BasicTransferForm {

    public static void main(String[] args) throws Exception {
        // 创建输入流
        FileInputStream fileInputStream = new FileInputStream("E:\\123.png");

        // 创建文件管道
        FileChannel inputChannel = fileInputStream.getChannel();

        // 创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\123copy.png");

        // 创建文件管道
        FileChannel outputChannel = fileOutputStream.getChannel();

        // 将输入流中的文件拷贝到输出流中
        outputChannel.transferFrom(inputChannel,0,inputChannel.size());

        // 关闭流以及通道
        outputChannel.close();
        inputChannel.close();

        fileOutputStream.close();
        fileInputStream.close();
    }


}
