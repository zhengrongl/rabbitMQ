package com.xiaoliu.comsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 1:34
 * @description:
 *    发布订阅消费者代码
 */
public class ConsumerRouting2 {


    public static void main(String[] args) {
        // 1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 2.设置参数

        // IP
        factory.setHost("localhost");

        // 端口
        factory.setPort(5672);

        // 虚拟机
        factory.setVirtualHost("/demo");

        // 账号密码
        factory.setUsername("admin");
        factory.setPassword("admin");


        try {
            // 3.创建连接 Connection
            Connection connection = factory.newConnection();
            // 4.创建Channel
            Channel channel = connection.createChannel();

            String queue2Name = "test_direct_queue2";


            // 6.接收消息
            /**
             * basicConsume(String queue, boolean autoAck, Consumer callback)
             * 参数:
             *   1. queue: 队列名称
             *   2. autoAck: 是否自动确认
             *   3. callback: 回调对象
             */
            DefaultConsumer consumer = new DefaultConsumer(channel){
                /**
                 * 回调方法，当收到消息后会自动执行该方法
                 * @param consumerTag 消息标识
                 * @param envelope 获取一些信息，交换机、路由key。。。
                 * @param properties 配置信息
                 * @param body 数据
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    System.out.println("consumerTag:" + consumerTag);
//                    System.out.println("Exchange:" + envelope.getExchange());
//                    System.out.println("routingKey:" + envelope.getRoutingKey());
//                    System.out.println("properties:" + properties);
                    System.out.println("body:" + new String(body));
                    System.out.println("将日志信息保存数据库....");
                }
            };
            channel.basicConsume(queue2Name,true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
