package com.netty.nio;

import java.nio.IntBuffer;


/**
 * 与BIO的比较：
 * 1：流：BIO，块：NIO，块IO处理数据效率大于流数据
 * 2：BIO阻塞，NIO：非阻塞
 * 3：BIO：基于字节与字符；NIO：基于channel（通道）和Buffer（缓冲区），数据从通道读取到缓冲区或从缓冲区写入通道，由于数据量到达一定程度才进行读取或写入，所以不会造成阻塞
 */
public class Buffer {

    public static void main(String[] args) {
        // 创建一个Buffer 大小为5
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //向Buffer中存放数据
        for (int i = 0; i< intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 从buffer中读取数据
        // 将buffer转换，读写切换
        intBuffer.flip();

        // 当buffuer中有数据时
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
