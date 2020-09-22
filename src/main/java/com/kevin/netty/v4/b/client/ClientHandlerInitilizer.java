package com.kevin.netty.v4.b.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientHandlerInitilizer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		 ch.pipeline()
         //空闲检测
         .addLast(new StringDecoder())
         .addLast(new StringEncoder())
         .addLast(new HeartbeatHandler())
         .addLast(new ClientHandler())
         ;
	}


}
