package com.kevin.disruptor.consumer;

import com.kevin.disruptor.entity.LongEvent;
import com.lmax.disruptor.EventHandler;

//消费者获取生产推送数据
public class LongEventHandler implements EventHandler<LongEvent> {

	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		System.out.println("消费者1 获取生产者数据..event:" + event.getValue());
	}

}
