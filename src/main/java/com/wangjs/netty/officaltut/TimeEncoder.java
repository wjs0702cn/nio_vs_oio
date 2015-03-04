package com.wangjs.netty.officaltut;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import static org.jboss.netty.buffer.ChannelBuffers.*;

public class TimeEncoder extends SimpleChannelHandler{

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		UnixTime time = (UnixTime)e.getMessage();
		ChannelBuffer buffer = buffer(8);
		buffer.writeLong(time.getTime());
		
		Channels.write(ctx, e.getFuture(), buffer);
	}
	

}
