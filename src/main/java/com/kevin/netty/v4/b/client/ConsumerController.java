package com.kevin.netty.v4.b.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.netty.v4.b.NettyClient2;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ConsumerController {
    @Autowired
    private NettyClient2 nettyClient;

    @GetMapping("/send")
    public String send() {
        String message = "你好";
		nettyClient.sendMsg(message);
        log.info("向服务端发送:{}",message);
        return "send ok";
    }
}