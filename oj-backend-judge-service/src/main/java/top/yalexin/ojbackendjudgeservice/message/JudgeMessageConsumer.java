package top.yalexin.ojbackendjudgeservice.message;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import top.yalexin.ojbackendjudgeservice.judge.JudgeService;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@Slf4j
public class JudgeMessageConsumer {

    @Resource
    private JudgeService judgeService;

    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag) {
        log.info("receiveMessage msg = {}", message);
        try {
            long questionSubmitId = Long.parseLong(message);
            judgeService.doJudge(questionSubmitId);
            channel.basicAck(deliverTag, false);
        } catch (Exception e) {
            // 如果发生错误，则重新入队
            log.error(String.valueOf(e));
            try {
                channel.basicNack(deliverTag, false, false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
