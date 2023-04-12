package jg.actionfigures.server.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jg.actionfigures.server.API.MessageRepository;
import jg.actionfigures.server.Models.Cassandra.Message;

@RestController
@RequestMapping("/test")
public class MainController {

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/xd")
    public String test() {
        return "тест";
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        List<Message> messages = (List<Message>) messageRepository.findAll();
        return messages;
    }

    @PostMapping("/update")
    public void sendMessages(@RequestBody Message message) {
        messageRepository.save(message);
    }
}
