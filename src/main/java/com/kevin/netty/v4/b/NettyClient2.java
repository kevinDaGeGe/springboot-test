package com.kevin.netty.v4.b;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kevin.netty.v4.b.client.ClientHandlerInitilizer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NettyClient2  {
    private EventLoopGroup group = new NioEventLoopGroup();
    @Value("${netty.port}")
    private int port;
    @Value("${netty.host}")
    private String host;    
    private SocketChannel socketChannel;

    public void sendMsg(String message)  {
        socketChannel.writeAndFlush(message);
/*        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<String> service = new ExecutorCompletionService<String>(executor);

        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            Future<String> future = executor.submit(() -> {
                System.out.println("task["+ finalI +"] started!");
                Thread.sleep(1000*(3-finalI));// cost some time
                System.out.println("task["+ finalI +"]finished!");
                return "result["+ finalI +"]";
            });
            futureList.add(future);
        }

        for (Future<String> future : futureList) {
            System.out.println(future.get());

        }
        System.out.println("Main thread finished.");
        executor.shutdown();
*/    }

    @PostConstruct
    public void start()  {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientHandlerInitilizer());
        ChannelFuture future = bootstrap.connect();
        //客户端断线重连逻辑
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                log.info("连接Netty服务端成功,port:"+port);
            } else {
                log.info("连接失败，进行断线重连");
                future1.channel().eventLoop().schedule(() -> start(), 20, TimeUnit.SECONDS);
            }
        });
        socketChannel = (SocketChannel) future.channel();
    }
}