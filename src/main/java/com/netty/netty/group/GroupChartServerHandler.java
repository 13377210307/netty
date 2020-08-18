package com.netty.netty.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChartServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个channel组管理所有的channel，GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // 表示建立连接，一旦连接将第一个执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        // 将该客户聊天的信息推送给其他在线的客户端
        // 此方法会将channelGroup中所有的channel遍历，并发送消息，我们不需要自己遍历
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+" 加入聊天"+sdf.format(new Date())+"\n");
        channelGroup.add(channel);
    }

    // 断开连接，将xx的离线消息推送给其他客户端
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        ctx.writeAndFlush("[客户端]"+channel.remoteAddress()+" 离开了聊天室");
    }

    // 表示channel正在活跃状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 上线了");
    }

    // 表示channel不活跃状态，提示xx下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 已离线");
    }

    // 异常处理，关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        // 获取当前通道
        Channel channel = ctx.channel();
        // 遍历channelGroup，根据不同的情况，回送不同消息
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                // 不是当前的channel转发消息
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送了消息"+s+"\n");
            }else {
                // 回显自己发送的消息给自己
                ch.writeAndFlush("[自己]"+s+"\n");
            }
        });


    }
}
