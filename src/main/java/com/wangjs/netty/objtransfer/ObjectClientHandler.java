package com.wangjs.netty.objtransfer;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class ObjectClientHandler extends SimpleChannelHandler{

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		e.getChannel().write(new Command("trans data"));
	}

}
