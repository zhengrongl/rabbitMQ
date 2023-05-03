# 黑马RabbitMQ

## 1、MQ的基本概念

### 1.1 MQ概述

MQ全称**M**essage **Q**ueue（消息队列）,是在消息的传输过程中保存消息的容器。多用于分布式系统之间进行通信。



1.远程调用

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326144550468.png)





2.通过第三方传递消息

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326144651334.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)





**小结**

- MQ，消息队列，存储消息的中间件
- 分布式系统通信两种方式:  直接通过远程调用和 借助第三方完成间接通信
-  发送方称为生产者，接收方称为消费者



### 1.2 MQ的优势和劣势

优势:

- 应用解耦
- 异步提速
- 削峰填谷



劣势:

- 系统可用性降低
- 系统复杂度提高
- 一致性问题



### 1.3 MQ的优势

#### 1.应用解耦

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326145831499.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)



**系统的耦合性越高，容错性就越低，可维护性就越低**



通过MQ实现解耦

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326150410871.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)



#### 2.异步提速

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326150618648.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

一个下单操作耗时：20+300+300+300 = 920ms
用户点击完下单按钮后，需要等待920ms才能得到下单响应，太慢！



- MQ方式通信：异步

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326150852924.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

用户点击完下单按钮后，只需等待25ms就能得到下单响应（20+5 = 25ms）
提升用户体验和系统吞吐量（单位时间内处理请求的数目）。





#### 3.削峰填谷

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326151838993.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

请求瞬间增多，导致A系统压力过大而宕机。



MQ传递

![在这里插入图片描述](https://img-blog.csdnimg.cn/2021032615200161.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326152049113.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

使用了MQ之后，限制消费小溪的速度为1000，这样一来，高峰期产生的数据势必会挤压在MQ中，高峰就被“削”掉了，但是因为消息积压，在高峰期过后的一段时间内，消费消息的速度还是会维持在1000，直到消费完积压的消息，这就叫做“**填谷**”。
使用MQ后，可以提高系统稳定性。



小结:

1、应用解耦：提高系统容错性和可维护性
2、异步提速：提升用户体验和系统吞吐量
3、削峰填谷：提高系统稳定性。



### 1.4 MQ的劣势

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326152739968.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)



- 系统可用性降低

  系统引入的外部依赖越多，系统稳定性就越差，一旦MQ宕机，就会对业务造成影响，如何保证MQ的高可用？

  

- 系统复杂度提高

  MQ的加入大大增加了系统的复杂度，以前系统都是同步的远程调用，现在是通过MQ进行异步调用，如何保证消息没有被重复消费？怎么处理消息丢失情况?那么保证消息传递的顺序性？

  

- 一致性问题

  A系统处理完业务，通过MQ给B、C、D三个系统发消息数据，如果B系统、C系统处理成功、D系统处理失败。如何保证消息数据处理的一致性?



小结：

既然MQ有优势也有劣势，那么使用MQ需要满足什么条件呢？

1. 生产者不需要从消费出获得反馈，引入消息队列之前的直接调用，其接口的返回值应该为空，这才让明明下层的动作还没做，上层却当成动作做完了继续往后走，即所谓异步成为了可能。
2. 容许短暂的不一致性。
3. 确实用了有效果。即解耦、提速、削峰这些方面的收益，超过加入MQ，管理MQ这些成本。



### 1.5 常见的MQ产品

目前业界有很多的MQ产品，例如RabbitMQ、RocketMQ、ActiveMQ、Kafka、ZeroMQ，MetaMq等，也有直接使用Redis充当消息队列的案例，而这些消息队列产品，各有侧重，在实际选型时，需要结合自身需求及MQ产品特征，综合考虑。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326155130522.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)



### 1.6 RabbitMQ 简介

**AMQP**，即 **Advanced Message Queuing Protocol** (高级消息队列协议)，是一个网络协议，是应用层协议的一个开放标准，为面向消息的中间件设计。基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品，不同的开发语言条件的限制。2006年，AMQP规范发布。类比**HTTP**。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326160432214.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

2007年，Rabbit计数公司给予AMQP标准开发的RabbitMQ 1.0 发布。RabbitMQ采用Erlang语言开发。
Erlang语言由Ericson设计，专门为开发高并发和分布式系统的一种语言，在典型领域使用广泛。



RabbitMQ基础架构如下图：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326161047667.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)



RabbitMQ中的相关概念：

- **Broker**: 接收和分发消息的应用，RabbitMQ Server就是Message Broker

  

- **Virtual Host**: 出于多租户和安全因素设计的，把AMQP的基本组件划分到一个虚拟的分组中，类似于网络中的namespace概念。

  当多个不同的用户使用相同一个RabbitMQ Server提供的服务时，可以划分出多个vhost，

  每个用户在自己的vhost创建exchange(交换机)/queue(消息队列)等

  

- **Connection**: publisher(生产者)/consumer(消费者)和broker之间的TCP连接

  

- **Channel**: 如果每一次访问RabbitMQ都建立一个Connection，在消息量大的时候建立TCP Connection的开销将是巨大的，效率也较低。Channel是在connection内部建立的逻辑连接，如果应用程序支持多线程，通常每一个thread创建单独的channel进行通讯，AMQP method包含了channel id帮助客户端和message broker识别channel，所以channel之间是完全隔离的。Channel作为轻量级的Connection极大减少了操作系统建立TCP connection的开销。

  

- **Exchange**: Message 到达Broker的第一站，根据分发规则，匹配查询表中的routing key(路由Key)，分发消息到queue中去，常用的类型有: direct(point-to-point)，topic(publish-subscribe) and fanout(multicast)

  

- **Queue**: 消息最终被送到这里等待consumer(消费者)取走

  

- **Binding**： exchange 和 queue之间的虚拟连接，binding中可以包含routing key(路由 key)。Binding信息被保存到exchange中的查询表中，用于message的分发依据。



RabbitMQ提供了6种工作模式：

1. 简单模式
2. work queues 工作队列模式
3. Publish/Subscribt 发布与订阅模式
4. Routing 路由模式
5. Topics 主题模式
6. RPC远程调用模式(远程调用，不太算MQ)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326163033586.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210326163104880.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)



### 1.7 JMS

JMS即Java消息服务（JavaMessage Service）应用程序接口，是一个Java平台中关于面向消息中间件的API

JMS是JavaEE规范中的一种，类似于JDBC

很多消息中间件都实现了JMS规范，例如：ActiveMQ。RabbitMQ官方没有提供JMS的实现包，但是开源社区有



小结:

1.RabbitMQ是基于AMQP协议使用Erlang语言开发的一款消息队列产品

2.RabbitMQ提供了6种工作模式，我们学习5种。

3.AMQP是协议，类比HTTP。

4.JMS是API规范接口，类比JDBC



## 2.RabbitMQ的安装和配置

RabbitMQ的官方地址：http://www.rabbitmq.com/

（个人学习建议直接使用win版docker或者RabbitMQ进行下载安装，这样比较快，方便）



```xml
<dependencies>
        <!-- rabbitMQ 依赖 -->
        <dependency>
            <groupId>com.rabbitmq</groupId>
            <artifactId>amqp-client</artifactId>
            <version>5.7.3</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.8.0</version>
            </plugin>
        </plugins>
    </build>
```





## 3.RabbitMQ快速入门

### 3.1 入门程序

需求: 使用简单模式完成消息传递

步骤：
① 创建工程（生产者、消费者）
② 分别添加依赖
③ 编写生产者发送消息
④ 编写消费者接收消息

```java
package com.xiaoliu.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: xiaoliu
 * @date: 2023/5/3
 * @time: 0:16
 * @description:
 *     简单模式生产者代码
 */
public class ProducerHelloWorld {

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
            channel.queueDeclare("hello_world",true,false,false,null);

            // 6.发送消息
            /**
             * basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
             * 参数:
             *    1. exchange: 交换机名称，简单模式下交换机会使用默认的 ""
             *    2. routingKey: 路由key
             *    3. props: 配置信息
             *    4. body: 发送消息数据
             */
            String body = "Hello RabbitMQ~";
            channel.basicPublish("","hello_world",null,body.getBytes());

            // 7.释放资源
            channel.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

控制台打印结果:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```



```java
package com.xiaoliu.comsumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 1:34
 * @description:
 *    简单模式消费者代码
 */
public class ConsumerHelloWorld {


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
            channel.queueDeclare("hello_world",true,false,false,null);

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
                    System.out.println("consumerTag:" + consumerTag);
                    System.out.println("Exchange:" + envelope.getExchange());
                    System.out.println("routingKey:" + envelope.getRoutingKey());
                    System.out.println("properties:" + properties);
                    System.out.println("body:" + new String(body));
                }
            };
            channel.basicConsume("hello_world",true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

控制台打印结果:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
consumerTag:amq.ctag-QrZ3AJ7ZeBREOeOp8QGfaw
Exchange:
routingKey:hello_world
properties:#contentHeader<basic>(content-type=null, content-encoding=null, headers=null, delivery-mode=null, priority=null, correlation-id=null, reply-to=null, expiration=null, message-id=null, timestamp=null, type=null, user-id=null, app-id=null, cluster-id=null)
body:Hello RabbitMQ~
```





### 3.2 小结

上述的入门案例中其实使用的是如下的简单模式：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210327154215326.png)

在上图的模型中，有以下概念：
① P：生产者，也就是要发送消息的程序
② C：消费者：消息的接收者，会一直等待消息到来
③ queue：消息队列，图中红色部分。类似一个邮箱，可以缓存消息；生产者向其中投递消息，消费者从其中取出消息



## 4.RabbitMQ的工作模式

### 4.1、Work queues工作队列模式

1、模式说明（一条消息只能被一个消费者消费）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210327155354414.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

- **Work Queues**：与入门程序的简单模式相比，多了一个或一些消费端，多个消费端共同消费同一个队列中的消息。
- **应用场景**：对于任务过重或任务较多情况使用工作队列可以提高任务处理的速度。



```java
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

```



```java
package com.xiaoliu.comsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 1:34
 * @description:
 *    工作模式消费者代码
 */
public class ConsumerWorkQueues {


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
                }
            };
            channel.basicConsume("work_queues",true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```java
package com.xiaoliu.comsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 1:34
 * @description:
 *    工作模式消费者代码
 */
public class ConsumerWorkQueues2 {


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
                }
            };
            channel.basicConsume("work_queues",true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```



小结:

1、在一个队列中如果有多个消费者，那么消费者之间对于同一个消息的关系是**竞争**关系。
2、**Work Queues**对于任务过重或任务较多情况使用工作队列模式可以提高任务处理的速度。例如：短信服务部署多个，需要有一个节点发送即可。





### 4.2 Pub/Sub订阅模式

1.模式说明（一条消息可以被多个消费者消费）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210327160534261.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bhbl9oMTk5NQ==,size_16,color_FFFFFF,t_70)

在订阅模型中，多了一个Exchange角色，而且过程略有变化:

- P: 生产者，也就是要发送消息的程序，但是不再发送到队列中，而是发送给X（交换机）

- C: 消费者，消息的接收者，会一直等待消息到来

- Queue: 消息队列，接收消息、缓存消息

- Exchange: 交换机(X)，一方面，接收生产者发送的消息。另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于Exchange的类型。Exchange有常见以下3种类型：

  - Fanout: 广播，将消息交给所有绑定交换机的队列
  - Direct: 定向，把消息交给符合指定routing Key的队列
  - Topic: 通配符，把消息交给符合routing pattern （路由模式）的队列

  **Exchange** （交换机）只负责转发消息，不具备存储消息的能力，因此如果没有任何对了与Exchange绑定，或者没有符合路由规则的队列，那么消息会丢失！



```java
package com.xiaoliu.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 0:16
 * @description:
 *     发布订阅模式生产者代码
 */
public class ProducerPubSub {

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


            /**
             * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
             * 参数:
             *    1. exchange： 交换机名称
             *    2. type: 交换机类型
             *     DIRECT("direct"), ： 定向
             *     FANOUT("fanout"), ： 扇形(广播)，发送消息到每一个与之绑定的队列
             *     TOPIC("topic"), ： 通配符的方式
             *     HEADERS("headers"); ： 参数匹配
             *    3. durable: 是否持久化
             *    4. autoDelete: 是否删除
             *    5. internal: 内部使用。一般为false
             *    6. arguments： 参数
             */
            // 5.创建交换机
            String exchangeName = "test_fanout";
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT,true,false,false,null);

            // 6.创建队列
            String queue1Name = "test_fanout_queue1";
            String queue2Name = "test_fanout_queue2";
            channel.queueDeclare(queue1Name,true,false,false,null);
            channel.queueDeclare(queue2Name,true,false,false,null);

            /**
             * queueBind(String queue, String exchange, String routingKey)
             * 参数:
             *    1. queue: 队列名称
             *    2. exchange：交换机名称
             *    3. routingKey: 路由键，绑定规则
             *       如果交换机的类型为fanout，routingKey设置为""
             */
            // 7.绑定队列和交换机
            channel.queueBind(queue1Name,exchangeName,"");
            channel.queueBind(queue2Name,exchangeName,"");

            // 8.发送消息
            String body = "日志信息: 张三调用了findAll方法...日志级别为:INFO...";
            channel.basicPublish(exchangeName,"",null,body.getBytes());

            // 9.释放资源
            channel.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

控制台打印:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

Process finished with exit code 0


```





```java
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
public class ConsumerPubSub1 {


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

            String queue1Name = "test_fanout_queue1";



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
                    System.out.println("将日志信息打印到控制台....");
                }
            };
            channel.basicConsume(queue1Name,true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


控制台打印:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
body:日志信息: 张三调用了findAll方法...日志级别为:INFO...
将日志信息打印到控制台....
```



```java
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
public class ConsumerPubSub2 {


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

            String queue2Name = "test_fanout_queue2";


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


控制台打印:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
body:日志信息: 张三调用了findAll方法...日志级别为:INFO...
将日志信息保存数据库....
```



### 4.3 Routing路由模式

1、模式说明：

- 队列与交换机的绑定，不能是任意绑定了，而是要指定一个routingKey（路由key）
- 消息的发送方在向exchange发送消息时，也必须指定消息的routingKey
- Exchange不在把消息交给每一个绑定的队列，而是根据消息的RoutingKey进行判断，只有队列的RoutingKey与消息的RoutingKey完全一致，才会接收到消息



![在这里插入图片描述](https://img-blog.csdnimg.cn/2021033011224388.png)



图解：

- p：生产者，向exchange发送消息，发送消息时，会制定一个routing key
- X：exchange（交换机），接收生产者消息，然后把消息递交给routing key完全匹配的队列
- C1：消费者，其所在队列指定需要routing key为error的消息
- C2：消费者，其所在队列指定需要routing key为info、error、warning的消息





```java
package com.xiaoliu.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 0:16
 * @description:
 *     路由模式生产者代码
 */
public class ProducerRouting {

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


            /**
             * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
             * 参数:
             *    1. exchange： 交换机名称
             *    2. type: 交换机类型
             *     DIRECT("direct"), ： 定向
             *     FANOUT("fanout"), ： 扇形(广播)，发送消息到每一个与之绑定的队列
             *     TOPIC("topic"), ： 通配符的方式
             *     HEADERS("headers"); ： 参数匹配
             *    3. durable: 是否持久化
             *    4. autoDelete: 是否删除
             *    5. internal: 内部使用。一般为false
             *    6. arguments： 参数
             */
            // 5.创建交换机
            String exchangeName = "test_direct";
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,true,false,false,null);

            // 6.创建队列
            String queue1Name = "test_direct_queue1";
            String queue2Name = "test_direct_queue2";
            channel.queueDeclare(queue1Name,true,false,false,null);
            channel.queueDeclare(queue2Name,true,false,false,null);

            /**
             * queueBind(String queue, String exchange, String routingKey)
             * 参数:
             *    1. queue: 队列名称
             *    2. exchange：交换机名称
             *    3. routingKey: 路由键，绑定规则
             *       如果交换机的类型为fanout，routingKey设置为""
             */
            // 7.绑定队列和交换机
            // 队列1绑定 : error
            channel.queueBind(queue1Name,exchangeName,"error");

            // 队列2绑定: info,error,warning
            channel.queueBind(queue2Name,exchangeName,"info");
            channel.queueBind(queue2Name,exchangeName,"error");
            channel.queueBind(queue2Name,exchangeName,"warning");

            // 8.发送消息
            String body = "日志信息: 张三调用了delete方法...日志级别为:ERROR...";
            //channel.basicPublish(exchangeName,"error",null,body.getBytes());
            //channel.basicPublish(exchangeName,"info",null,body.getBytes());
            channel.basicPublish(exchangeName,"warning",null,body.getBytes());

            // 9.释放资源
            channel.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }




    }


}

控制台打印:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

Process finished with exit code 0
```



队列一只能接收到: error路由key的日志信息

```java
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
public class ConsumerRouting1 {


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

            String queue1Name = "test_direct_queue1";



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
                    System.out.println("将日志信息打印到控制台....");
                }
            };
            channel.basicConsume(queue1Name,true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

控制台打印：
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
body:日志信息: 张三调用了delete方法...日志级别为:ERROR...
将日志信息打印到控制台....
```



队列二能接收到: info、error、warning的日志信息

```java
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
public class ConsumerRouting1 {


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

            String queue1Name = "test_direct_queue1";



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
                    System.out.println("将日志信息打印到控制台....");
                }
            };
            channel.basicConsume(queue1Name,true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


控制台打印:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
body:日志信息: 张三调用了findAll方法...日志级别为:INFO...
将日志信息保存数据库....
body:日志信息: 张三调用了delete方法...日志级别为:ERROR...
将日志信息保存数据库....
body:日志信息: 张三调用了insert方法...日志级别为:WARNING...
将日志信息保存数据库....
```



小结：
**Routing**模式要求队列在绑定交换机时要指定**routing key**，消息会转发到符合routing key的队列





### 4.4 Topic 通配符模式

1.模式说明

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210330144622534.png)

图解：

- 红色Queue：绑定的usa.#，因此凡是以usa.开头的routing key 都会被匹配到；
- 黄色Queue：绑定的是#.news，因此凡是以.news结尾的routing key都会被匹配到；
- *代表1个单词，#代表0到多个单词。



```java
package com.xiaoliu.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 0:16
 * @description:
 *     Topic通配符模式生产者代码
 */
public class ProducerTopic {

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


            /**
             * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
             * 参数:
             *    1. exchange： 交换机名称
             *    2. type: 交换机类型
             *     DIRECT("direct"), ： 定向
             *     FANOUT("fanout"), ： 扇形(广播)，发送消息到每一个与之绑定的队列
             *     TOPIC("topic"), ： 通配符的方式
             *     HEADERS("headers"); ： 参数匹配
             *    3. durable: 是否持久化
             *    4. autoDelete: 是否删除
             *    5. internal: 内部使用。一般为false
             *    6. arguments： 参数
             */
            // 5.创建交换机
            String exchangeName = "test_topic";
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,false,null);

            // 6.创建队列
            String queue1Name = "test_topic_queue1";
            String queue2Name = "test_topic_queue2";
            channel.queueDeclare(queue1Name,true,false,false,null);
            channel.queueDeclare(queue2Name,true,false,false,null);

            /**
             * queueBind(String queue, String exchange, String routingKey)
             * 参数:
             *    1. queue: 队列名称
             *    2. exchange：交换机名称
             *    3. routingKey: 路由键，绑定规则
             *       如果交换机的类型为fanout，routingKey设置为""
             */
            // 7.绑定队列和交换机 routingKey 系统的名称.日志的级别
            // 需求：所有error接别日志存入数据，所有order系统的日志存入数据库
            channel.queueBind(queue1Name,exchangeName,"#.error");
            channel.queueBind(queue1Name,exchangeName,"order.*");
            channel.queueBind(queue2Name,exchangeName,"*.*");

            // 8.发送消息
            String body = "日志信息: 张三调用了findAll方法...日志级别为:INFO...";
//            channel.basicPublish(exchangeName,"order.error",null,body.getBytes());
//            channel.basicPublish(exchangeName,"goods.info",null,body.getBytes());
            channel.basicPublish(exchangeName,"goods.error",null,body.getBytes());

            // 9.释放资源
            channel.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }




    }


}

控制台打印:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

Process finished with exit code 0
```





```java
package com.xiaoliu.comsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 1:34
 * @description:
 *    Topic通配符消费者代码
 */
public class ConsumerTopic1 {


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

            String queue1Name = "test_topic_queue1";



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
                    System.out.println("将日志信息存入数据库....");
                }
            };
            channel.basicConsume(queue1Name,true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

控制台打印:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
body:日志信息: 张三调用了findAll方法...日志级别为:INFO...
将日志信息存入数据库....
body:日志信息: 张三调用了findAll方法...日志级别为:INFO...
将日志信息存入数据库....
```





```java
package com.xiaoliu.comsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 1:34
 * @description:
 *    Topic通配符消费者代码
 */
public class ConsumerTopic2 {


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

            String queue1Name = "test_topic_queue2";



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
                    System.out.println("将日志信息打印控制台....");
                }
            };
            channel.basicConsume(queue1Name,true,consumer);


            // 7.释放资源 不用关闭资源

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


控制台打印:
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
body:日志信息: 张三调用了findAll方法...日志级别为:INFO...
将日志信息打印控制台....
body:日志信息: 张三调用了findAll方法...日志级别为:INFO...
将日志信息打印控制台....
body:日志信息: 张三调用了findAll方法...日志级别为:INFO...
将日志信息打印控制台....
```





小结：
Topics模式可以实现Pub/Sub发布与订阅模式和Routing路由模式的功能，只是Topics在配置routing key的时候可以使用通配符，显得更加灵活



### 4.5 工作模式总结

1. 简单模式

   一个生产者、一个消费者，不需要设置交换机（使用默认的交换机）

   

2. 工作队列模式 Work Queue

   一个生产则、多个消费者（竞争关系），不需要设置交换机（使用默认的交换机）。

   

3. 发布订阅模式 Publish/Subscribe

   需要设置类型为**fanout**的交换机，并且交换机和队列进行绑定，当发送消息到交换机后，交换机将消息发送到绑定的队列。

   

4. 路由模式Routing

   需要设置类型为**direct**的交换机，交换机和队列进行绑定，并且指定routing Key，当发送消息到交换机后，交换机根据routingKey将消息发送到对应的队列。

   

5. 通配符模式Topic

   需要设置类型为**topic**的交换机，交换机和队列进行绑定，并且指定通配符方式的routingKey，当发送消息到交换机后，交换机会根据routingKey将消息发送到对应的队列。





## 5.Spring整合RabbitMQ

### 5.1 Spring整合RabbitMQ

需求: 使用Spring整合RabbitMQ

步骤：

   生产者:

​       ① 创建生产者工程

​       ②  添加依赖

​       ③ 配置整合

​       ④ 编写代码发送消息

​    消费者:

​       ① 创建消费者工程

​       ②  添加依赖

​       ③ 配置整合

​       ④ 编写消息监听器



maven依赖

```xml
<dependencies>

        <!-- Spring-context -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.7.RELEASE</version>
        </dependency>

        <!-- Spring-Rabbit -->
        <dependency>
            <groupId>org.springframework.amqp</groupId>
            <artifactId>spring-rabbit</artifactId>
            <version>2.1.8.RELEASE</version>
        </dependency>

        <!-- junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

        <!-- Spring-test -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.1.7.RELEASE</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.8.0</version>
            </plugin>
        </plugins>
    </build>
```





#### 生产者:

1.rabbitmq.properties

```properties
rabbitmq.host=localhost
rabbitmq.port=5672
rabbitmq.username=admin
rabbitmq.password=admin
rabbitmq.virtual-host=/demo
```



2.spring-rabbitmq-producer.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    <!--加载配置文件-->
    <context:property-placeholder location="classpath:rabbitmq.properties"/>

    <!-- 定义rabbitmq connectionFactory -->
    <rabbit:connection-factory id="connectionFactory"
                               host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"/>
    <!--定义管理交换机、队列-->
    <rabbit:admin connection-factory="connectionFactory"/>

    <!--定义持久化队列，不存在则自动创建；不绑定到交换机则绑定到默认交换机
    默认交换机类型为direct，名字为：""，路由键为队列的名称
    -->
    <!--
        id：bean的名称
        name：queue的名称
        auto-declare：自动创建
        auto-delete：自动删除。最后一个消费者和该队列断开连接后，自动删除队列
        exclusive：是否独占（排他）
        durable：是否持久化
    -->
    <rabbit:queue id="spring_queue" name="spring_queue"  auto-declare="true"/>

    <!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~广播；所有队列都能收到消息~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_fanout_queue_1" name="spring_fanout_queue_1" auto-declare="true"/>

    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_fanout_queue_2" name="spring_fanout_queue_2" auto-declare="true"/>

    <!--定义广播类型交换机；并绑定上述两个队列-->
    <rabbit:fanout-exchange id="spring_fanout_exchange" name="spring_fanout_exchange" auto-declare="true">
        <rabbit:bindings>
            <rabbit:binding queue="spring_fanout_queue_1"/>
            <rabbit:binding queue="spring_fanout_queue_2"/>
        </rabbit:bindings>
    </rabbit:fanout-exchange>
    
    <!-- 定义路由模式交换机 -->
    <rabbit:direct-exchange name="spring_direct_exchange">
        <rabbit:bindings>
            <!-- direct 类型的交换机绑定队列 key: 路由key  queue: 队列名称 -->
            <rabbit:binding queue="spring_queue" key="xxx"/>
        </rabbit:bindings>
    </rabbit:direct-exchange>

    <!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~通配符；*匹配一个单词，#匹配多个单词 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_star" name="spring_topic_queue_star" auto-declare="true"/>
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_well" name="spring_topic_queue_well" auto-declare="true"/>
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_well2" name="spring_topic_queue_well2" auto-declare="true"/>

    <rabbit:topic-exchange id="spring_topic_exchange" name="spring_topic_exchange" auto-declare="true">
        <rabbit:bindings>
            <rabbit:binding pattern="xiaoliu.*" queue="spring_topic_queue_star"/>
            <rabbit:binding pattern="xiaoliu.#" queue="spring_topic_queue_well"/>
            <rabbit:binding pattern="liu.#" queue="spring_topic_queue_well2"/>
        </rabbit:bindings>
    </rabbit:topic-exchange>

    <!--定义rabbitTemplate对象操作可以在代码中方便发送消息-->
    <rabbit:template id="rabbitTemplate" connection-factory="connectionFactory"/>
</beans>
```





3.测试代码:

```java
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
```





#### 消费者:

1.配置文件:rabbitmq.properties

```properties
rabbitmq.host=localhost
rabbitmq.port=5672
rabbitmq.username=admin
rabbitmq.password=admin
rabbitmq.virtual-host=/demo
```



2.spring-rabbitmq-consumer.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    <!--加载配置文件-->
    <context:property-placeholder location="classpath:rabbitmq.properties"/>

    <!-- 定义rabbitmq connectionFactory -->
    <rabbit:connection-factory id="connectionFactory" 
                               host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"/>

    <bean id="springQueueListener" class="com.xiaoliu.rabbitmq.listener.SpringQueueListener"/>
    <!--<bean id="fanoutListener1" class="com.itheima.rabbitmq.listener.FanoutListener1"/>
    <bean id="fanoutListener2" class="com.itheima.rabbitmq.listener.FanoutListener2"/>
    <bean id="topicListenerStar" class="com.itheima.rabbitmq.listener.TopicListenerStar"/>
    <bean id="topicListenerWell" class="com.itheima.rabbitmq.listener.TopicListenerWell"/>
    <bean id="topicListenerWell2" class="com.itheima.rabbitmq.listener.TopicListenerWell2"/>-->

    <rabbit:listener-container connection-factory="connectionFactory" auto-declare="true">
        <rabbit:listener ref="springQueueListener" queue-names="spring_queue"/>
        <!--<rabbit:listener ref="fanoutListener1" queue-names="spring_fanout_queue_1"/>
        <rabbit:listener ref="fanoutListener2" queue-names="spring_fanout_queue_2"/>
        <rabbit:listener ref="topicListenerStar" queue-names="spring_topic_queue_star"/>
        <rabbit:listener ref="topicListenerWell" queue-names="spring_topic_queue_well"/>
        <rabbit:listener ref="topicListenerWell2" queue-names="spring_topic_queue_well2"/>-->
    </rabbit:listener-container>
</beans>


```



3.实现监听器

```java
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

```



4.编写测试代码

```java
package com.xiaoliu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 16:23
 * @description:
 *     消费者测试代码
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-consumer.xml")
public class ConsumerTest {


    @Test
    public void test1() {
        boolean flag = true;
        while (flag) {

        }
    }

}
```



### 5.2 SpringBoot整合RabbitMQ

步骤:

**生产者**：

   1. 创建生产者SpringBoot工程

   2. 引入依赖坐标

      ```xml
      
      ```

3. 编写yml配置，基本信息配置
4. 定义交换机，队列以及绑定关系的配置类
5. 注入RabbitTemplate，调用方法，完成消息发送

#### 生产者

1.yml

```yaml
# 配置RabbitMQ的基本信息
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin
    virtual-host: /demo
```



2.ProducerApplication

```java
package com.xiaoliu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 16:49
 * @description:
 *     生产者启动类
 */
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class);
    }


}
```



3.Config配置类

```java
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
```



4.编写测试代码

```java
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

```





**消费者**：

   1. 创建生产者SpringBoot工程

   2. 引入依赖坐标

      ```xml
       <!-- 1.父工程依赖 -->
          <parent>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-parent</artifactId>
              <version>2.1.4.RELEASE</version>
          </parent>
      
          <dependencies>
      
              <!-- RabbitMQ -->
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-amqp</artifactId>
              </dependency>
      
              <!-- 单元测试 -->
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-test</artifactId>
              </dependency>
          </dependencies>
      
      ```

3. 编写yml配置，基本信息配置
4. 定义监听类，使用@RabbitListener注解完成队列监听



1.yml

```yaml
# 配置RabbitMQ的基本信息
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin
    virtual-host: /demo
```



2.启动类

```java
package com.xiaoliu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: admin
 * @date: 2023/5/3
 * @time: 17:23
 * @description:
 *    消费者启动类
 */
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
    }


}

```



3.监听类

```java
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

    @RabbitListener(queues = "boot_queue")
    public void listenerQueue(Message message) {
        System.out.println(message);
    }
}
```



4.直接开始启动我们的启动类就会收到消息

```tex
(Body:'boot mq Hello....' MessageProperties [headers={}, contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=true, receivedExchange=boot_topic_exchange, receivedRoutingKey=boot.haha, deliveryTag=1, consumerTag=amq.ctag-DwIPgzFFm6uRkdpFQxHAyg, consumerQueue=boot_queue])
```



小结:

- SpringBoot提供了快速整合RabbitMQ的方式
- 基本信息再yml中配置，队列交换机以及绑定关系在配置类中使用Bean的方式配置
- 生产端直接注入RabbitTemplate完成消息发送
- 消费端直接使用@RabbitListener完成消息接收





## 6.RabbitMQ高级特性

### 1.1 消息可靠投递

在使用RabbitMQ的时候，作为消息发送方希望杜绝任何消息丢失或者投递失败的场景。RabbitMQ为我们提供了两种方式用来控制消息的投递可靠性模式。

- confirm 确认模式
- return 退回模式



rabbitMQ整个消息投递的路径为:

producer-----> rabbitmq broker ------> exchange ------> queue ------> consumer

- 消息从producer 到 exchange 则会返回一个confirmCallback。
- 消息从exchange--->queue投递失败则会返回一个returnCallback。

我们将利用这两个callback控制消息的可靠性投递



maven依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.7.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.amqp</groupId>
            <artifactId>spring-rabbit</artifactId>
            <version>2.1.8.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.1.7.RELEASE</version>
        </dependency>
    </dependencies>
```



rabbitmq.properties

```properties
rabbitmq.host=localhost
rabbitmq.port=5672
rabbitmq.username=admin
rabbitmq.password=admin
rabbitmq.virtual-host=/demo
```



**生产者**:

Spring配置文件:spring-rabbitmq-producer.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    <!--加载配置文件-->
    <context:property-placeholder location="classpath:rabbitmq.properties"/>

    <!-- 定义rabbitmq connectionFactory -->
    <rabbit:connection-factory id="connectionFactory"
                               host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"
                               publisher-confirms="true"
                               publisher-returns="true"/>
    <!--定义管理交换机、队列-->
    <rabbit:admin connection-factory="connectionFactory" />

    <!--定义rabbitMqTemplate对象操作可以在代码中方便发送消息-->
    <rabbit:template id="rabbitTemplate" connection-factory="connectionFactory"/>

    <!--消息可靠性投递(生产端)-->
    <rabbit:queue id="test_queue_confirm" name="test_queue_confirm"></rabbit:queue>
    <rabbit:direct-exchange name="test_exchange_confirm">
        <rabbit:bindings>
            <rabbit:binding queue="test_queue_confirm" key="confirm"></rabbit:binding>
        </rabbit:bindings>
    </rabbit:direct-exchange>
</beans>
```



编写测试代码:

```java
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

```



小结：

1.设置ConnectionFactory的publisher-confirms = "true" 开启 确认模式。



2.使用rabbitTemplate.setConfirmCallback设置回调函数。当消息发送到exchange返回调用confirm方法。

在方法中判断ack，如果为true，则发送成功，如果为false，则发送失败，需要处理。



3.使用rabbitTemplate.setReturnCallback设置退回函数，当消息从exchange路由到queue失败后，如果设置了

rabbitTemplate.setMandatory(true)参数，则会将消息退回给producer。并执行回调函数returndMessage。



4.在RabbitMQ中也提供了事务机制，但是性能较差，此处不做讲解.

  使用channel下列方法，完成事务控制:

​          txSelect()，用于将当前channel设置成transaction模式

​          txCommit()，用于提交事务

​          txRollback()，用于回滚事务