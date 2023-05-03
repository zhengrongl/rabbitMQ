package com.xiaoliu.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 16:21
 * @description:
 *     消费者监听器
 */
public class SpringQueueListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 打印消息
        System.out.println(new String(message.getBody()));
    }
}
