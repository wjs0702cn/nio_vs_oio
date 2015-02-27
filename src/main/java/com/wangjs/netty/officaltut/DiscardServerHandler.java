package com.wangjs.netty.officaltut;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class DiscardServerHandler extends SimpleChannelHandler{

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
//		ChannelBuffer buf = (ChannelBuffer)e.getMessage();
//		
//		while(buf.readable()){
//			System.out.println((char)buf.readByte());
//			System.out.flush();
//		}
		
		// change to echo server
		Object message = e.getMessage();
		e.getChannel().write(message);
		System.out.println(message);
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		
		e.getChannel().close();
	}

}
