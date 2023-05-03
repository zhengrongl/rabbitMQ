package com.xiaoliu;

import com.xiaoliu.rabbitmq.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 17:01
 * @description:
 *     生产者测试代码
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {

    // 注入RabbitTemplate
    @Resource
    private RabbitTemplate rabbitTemplate;


    @Test
    public void testSend() {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.haha","boot mq Hello....");
    }




}
