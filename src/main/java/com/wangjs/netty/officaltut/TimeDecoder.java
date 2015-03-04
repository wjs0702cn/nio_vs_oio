package com.wangjs.netty.officaltut;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class TimeDecoder extends FrameDecoder{

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if(buffer.readableBytes()<8){
			System.out.println("buffer.readableBytes() = "+buffer.readableBytes());
			return null;
		}
//		return buffer.readBytes(8);
		// change bytes to POJO
		return new UnixTime(buffer.readLong());
	}
	

}
