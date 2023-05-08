package jg.actionfigures.server.API;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class MessageSender {

    private RabbitTemplate rabbitTemplate;

    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessageToUser(String login, String message) {
        rabbitTemplate.convertAndSend(login, message);
    }

}