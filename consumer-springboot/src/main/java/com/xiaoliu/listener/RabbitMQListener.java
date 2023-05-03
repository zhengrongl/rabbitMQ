package com.xiaoliu.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 17:28
 * @description:
 *    消息监听类
 */
@Component
public class RabbitMQListener {

    // 注解内添加监听的队列名称
    @RabbitListener(queues = "boot_queue")
    public void listenerQueue(Message message) {
        System.out.println(message);
    }




}
