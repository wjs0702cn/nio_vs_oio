package com.wangjs.netty.officaltut;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class TimeServerHandler extends SimpleChannelHandler{

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		Channel ch = e.getChannel();
		// netty给的教程有问题，4个字节发送int不够（时间已经超过了这个值）
		// 现在改用long来发送
		ChannelBuffer buffer = ChannelBuffers.buffer(8);
		//70*365*24*3600=约2207520000
		//NTP 协议中的时间是从1900年开始算起的。
		//而一些程序语言本身（比如PHP）中的时间戳是从1970年算起的，因此在计算时需要做一下调整。
		//不考虑润秒（Leap Seconds:每三年多一秒）的情况下，这个秒数差是 2208988800秒。
//		long currentLong = System.currentTimeMillis()/1000L+2208988800L;
//		int currentInt = (int)currentLong;
//		buffer.writeInt(currentInt);
		buffer.writeLong(System.currentTimeMillis()/1000L+2208988800L);
		ChannelFuture f = ch.write(buffer);
		
		f.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				future.getChannel().close();
			}
		});
	}
	

}
