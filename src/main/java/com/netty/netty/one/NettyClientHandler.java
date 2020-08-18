package com.netty.netty.one;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * 客户端处理类
 * 1：联通服务端，向服务端发送消息（channelActive）
 * 2：读取通道数据
 * 3：异常处理：关闭通道
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端："+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,服务端", CharsetUtil.UTF_8));
    }

    // 当通道有读取事件发生时会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 将信息转成ByteBuf
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println("服务器回复的消息："+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }

    // 异常处理

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
