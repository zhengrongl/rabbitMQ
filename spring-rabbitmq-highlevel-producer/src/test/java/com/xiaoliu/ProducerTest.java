package com.xiaoliu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 18:04
 * @description: 消息可靠性生产者测试代码
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 确认模式:
     * 步骤：
     * 1. 确认模式开启：ConnectionFactory中可开启 publisher-confirms="true"
     * 2. 在rabbitTemplate定义 ConfirmCallBack回调函数
     */
    @Test
    public void testConfirm() {
        // 2.定义回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 相关配置信息
             * @param ack exchange交换机是否成功收到了消息,true代表成功,false代表失败
             * @param cause 失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("confirm方法被执行了......");

                if (ack) {
                    // 接收成功
                    System.out.println("接收成功消息");
                } else {
                    // 接收失败
                    System.out.println("接收失败消息,原因：" + cause);

                    // 做一些处理，让消息再次发送
                }
            }
        });

        // 3.发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", "message confirm...");
    }

    /**
     * 回退模式: 当消息发送Exchange后，Exchange路由到Queue失败时，才会执行ReturnCallBack
     * 步骤：
     * 1. 开启回退模式：ConnectionFactory中可开启 publisher-returns="true"
     * 2. 设置ReturnCallBack
     * 3. 设置Exchange处理消息的模式:
     * 1. 如果消息没有路由到Queue，则丢弃消息(默认)
     * 2. 如果消息没有路由到Queue，返回给消息发送方ReturnCallBack
     */
    @Test
    public void testReturn() {

        // 设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory(true);

        // 2.设置ReturnCallBack
        rabbitTemplate.setReturnCallback(
                /**
                 *
                 * @param message 消息对象
                 * @param repIyCode 失败错误码
                 * @param replyText 错误信息
                 * @param exchange 交换机名称
                 * @param routingKey 路由key
                 */
                (message, repIyCode, replyText, exchange, routingKey) -> {
                    System.out.println("return 执行了.....");

                    System.out.println(message);
                    System.out.println(repIyCode);
                    System.out.println(replyText);
                    System.out.println(exchange);
                    System.out.println(routingKey);

                    // 处理
                });

        // 3.发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm111", "message confirm...");
    }


}
