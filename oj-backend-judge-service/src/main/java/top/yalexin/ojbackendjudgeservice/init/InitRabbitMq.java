package top.yalexin.ojbackendjudgeservice.init;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class InitRabbitMq {





    public static void doInit(){

        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("172.22.108.1");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = "code_exchange";
            channel.exchangeDeclare("code_exchange", "direct");

            // 创建队列，随机分配一个队列名称
            String queueName = "code_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "my_routingkey");
            log.info("启动成功");
        } catch (IOException | TimeoutException e) {
            log.error(String.valueOf(e));
        }

    }
}
