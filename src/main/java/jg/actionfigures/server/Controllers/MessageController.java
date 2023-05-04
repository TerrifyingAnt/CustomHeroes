package jg.actionfigures.server.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import jg.actionfigures.server.API.CassandraMessageRepository;
import jg.actionfigures.server.Models.Cassandra.Message;

@Controller
public class MessageController {

    @Autowired
    CassandraMessageRepository messageRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public Message sendMessage(@Payload Message chatMessage) {
        messageRepository.save(chatMessage);
        return chatMessage;
    }

}