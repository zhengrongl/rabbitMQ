package com.xiaoliu.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 0:16
 * @description:
 *     工作模式生产者代码
 */
public class ProducerWorkQueues {

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

            // 5.创建队列Queue
            /**
             * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
             * 参数:
             *   1. queue: 队列名称
             *   2. durable: 是否持久化,当mq重启之后，消息还会存在
             *   3. exclusive:
             *       * 是否独占，只能有一个消费者监听这个队列
             *       * 当connection关闭时，是否删除队列
             *   4. autoDelete: 是否自动删除，当没有Comsumer时，自动删除掉
             *   5. arguments： 参数
             */
            // 如果没有一个名字叫hello_world的队列，则会自动创建该队列，如果有则不会进行创建
            channel.queueDeclare("work_queues",true,false,false,null);

            // 6.发送消息
            /**
             * basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
             * 参数:
             *    1. exchange: 交换机名称，简单模式下交换机会使用默认的 ""
             *    2. routingKey: 路由key
             *    3. props: 配置信息
             *    4. body: 发送消息数据
             */
            for (int i = 1; i <= 10; i++) {
                String body = i + "Hello RabbitMQ~";
                channel.basicPublish("","work_queues",null,body.getBytes());
            }


            // 7.释放资源
            channel.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }




    }


}
