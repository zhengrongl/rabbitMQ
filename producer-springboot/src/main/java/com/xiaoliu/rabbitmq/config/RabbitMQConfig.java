package com.xiaoliu.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 16:50
 * @description:
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String QUEUE_NAME = "boot_queue";

    /**
     * 1.交换机
     */
    @Bean("bootExchange")
    public Exchange bootExchange() {
        return ExchangeBuilder
                // 交换机名称
                .topicExchange(EXCHANGE_NAME)
                // 是否持久化
                .durable(true)
                .build();
    }

    /**
     * 2.Queue 队列
     */
    @Bean("bootQueue")
    public Queue bootQueue() {
        return QueueBuilder
                // 队列名称
                .durable(QUEUE_NAME)
                .build();
    }

    /**
     * 3.队列和交换机绑定关系  Binding
     *
     *    1.知道哪个队列
     *    2.知道哪个交换机
     *    3.routing Key
     */
    @Bean
    public Binding bindingQueueExchange(@Qualifier("bootQueue") Queue queue,
                                        @Qualifier("bootExchange") Exchange exchange) {
        return BindingBuilder
                // 绑定的队列
                .bind(queue)
                // 绑定的交换机
                .to(exchange)
                // 路由Key
                .with("boot.#")
                // 不需要带条件，如果需要则使用and()
                .noargs();

    }





}
