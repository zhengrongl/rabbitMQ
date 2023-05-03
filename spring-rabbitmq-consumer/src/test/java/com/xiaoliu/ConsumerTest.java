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
