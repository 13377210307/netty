package com.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池机制
 *
 * 思路：
 * 1：创建一个线程池
 * 2：如果有客户端连接就创建一个线程与之通讯（单独写一个方法）
 *
 * 注意点：
 * 1：每连接到一个客户端就会产生一个新线程
 * 2：阻塞：若停滞在某个步骤则为阻塞，如当客户端连接完成之后，如果没有发送消息，则会一直阻塞在客户端连接的地方
 * 阻塞点
 *   final Socket socket = serverSocket.accept();
 *   int read = inputStream.read(bytes);
 *
 * 存在问题：
 * 1：每个请求都需要创建独立的线程，与对应的客户端进行数据read业务处理以及数据write
 * 2：当并发数较大时，需要创建大量线程来处理连接，系统资源占用较大
 * 3：连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在read操作上，造成线程资源浪费
 */
public class BIOServer {

    public static void main(String[] args) throws Exception {


        // 创建一个线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // 创建serversocket
        ServerSocket serverSocket = new ServerSocket(6279);

        System.out.println("客户端启动了");

        while (true) {

            // 监听等待客户端连接
            final Socket socket = serverSocket.accept();

            System.out.println("连接到一个客户端");

            // 创建一个线程与之通讯
            newCachedThreadPool.execute(new Runnable() {
                // 重写run方法
                @Override
                public void run() {
                    // 与客户端进行通讯
                    handler(socket);
                }
            });
        }
    }


    // 与客户端通讯方法
    private static void handler(Socket socket) {
        byte[] bytes = new byte[1024];

        try {
            //通过socket读取数据
            InputStream inputStream = socket.getInputStream();

            // 循环读取客户端发送的数据
            while (true) {
                int read = inputStream.read(bytes);

                if (read != -1) {
                    // 数据未读取完毕 参数一：读取字节数，0：从第一个数开始读取，read：读取字节总数
                    System.out.println(new String(bytes,0,read));
                }else {
                    // 数据读取完毕进行break
                    break;
                }
            }
        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            System.out.println("客户端关闭连接");
            // 关闭socket
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
