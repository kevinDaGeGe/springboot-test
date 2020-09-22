
package com.kevin.netty.v4.a;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

// netty客户端
public class NettyClient1 {
	public static void main(String[] args) throws InterruptedException {
		String host = "127.0.0.1";
		int port = 8090;
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup); // (2)
			b.channel(NioSocketChannel.class); // (3)
			b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
//			b.option(ChannelOption.SO_BACKLOG, 120);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
						.addLast(new StringDecoder())
				        .addLast(new StringEncoder())
						.addLast(new ChannelInboundHandlerAdapter() {            //4
	                         @Override
	                         public void channelActive(ChannelHandlerContext ctx) throws Exception {
	                             ctx.writeAndFlush("123").addListener(ChannelFutureListener.CLOSE);//5
	                         }
	                         @Override
	                         public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	                             /**flush：将消息发送队列中的消息写入到 SocketChannel 中发送给对方，为了频繁的唤醒 Selector 进行消息发送
	                              * Netty 的 write 方法并不直接将消息写如 SocketChannel 中，调用 write 只是把待发送的消息放到发送缓存数组中，再通过调用 flush
	                              * 方法，将发送缓冲区的消息全部写入到 SocketChannel 中
	                              * */
	                             ctx.flush();
	                         }
	                     });
				}
			});
			// Start the client.
			ChannelFuture f = b.connect(host, port).sync(); // (5)
			System.out.println("client start");
			f.channel().writeAndFlush("hello world!");
			
			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
		
	}
}
