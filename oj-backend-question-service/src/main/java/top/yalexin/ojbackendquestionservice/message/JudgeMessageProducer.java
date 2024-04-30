package top.yalexin.ojbackendquestionservice.message;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 给判题机提供消息
 */
@Component
public class JudgeMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${message-queue.exchange}")
    private String exchange;

    @Value("${message-queue.routingKey}")
    private String routingKey;

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
