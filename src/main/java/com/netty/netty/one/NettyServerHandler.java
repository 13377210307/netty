package com.netty.netty.one;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取实际数据
    /**
     * ChannelHandlerContext：上下文对象，含有 管道piepeline，通道：channel，地址
     * Object msg：客户端发送的数据，默认为object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程："+Thread.currentThread().getName());
        System.out.println("server ctx="+ctx);
        Channel channel = ctx.channel();
        ctx.pipeline();  //本质是一个双向连接


        // 将msg转成ByteBuf(netty提供）
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("客户端发送的消息为："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址为："+channel.remoteAddress());
    }

    // 读取数据完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓存，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端",CharsetUtil.UTF_8));
    }


    // 处理异常，关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
