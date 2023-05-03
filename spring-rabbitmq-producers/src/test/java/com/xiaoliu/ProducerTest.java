package com.xiaoliu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 16:01
 * @description:
 *     生产者测试代码
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {

    // 1.注入 RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testHelloWorld() {
        // 2.发送消息
        rabbitTemplate.convertAndSend("spring_queue","Hello World Spring");
    }

    /**
     * 发送fanout消息
     */
    @Test
    public void testFanout() {
        // 2.发送消息
        rabbitTemplate.convertAndSend("spring_fanout_exchange","","spring fanout....");
    }

    /**
     * 发送Topic消息
     */
    @Test
    public void testTopic() {
        // 2.发送消息
        rabbitTemplate.convertAndSend("spring_topic_exchange","xiaoliu.hehe.haha","spring topic xiaoliu.*....");
    }



}
