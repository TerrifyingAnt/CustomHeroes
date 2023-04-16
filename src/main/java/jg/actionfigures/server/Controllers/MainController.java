package jg.actionfigures.server.Controllers;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jg.actionfigures.server.API.MessageRepository;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.AuthModule.Utils.JwtUtils;
import jg.actionfigures.server.AuthModule.Utils.TokenEnum;
import jg.actionfigures.server.Models.Cassandra.Message;
import jg.actionfigures.server.Models.PostgerSql.User;

@RestController
@RequestMapping("/test")
public class MainController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    private JwtUtils jwtUtils = new JwtUtils();

    @GetMapping("/xd")
    public String test() {
        return "тест";
    }

    @GetMapping("/messages")
    public List<Message> getMessages(HttpServletRequest request) {
        Long fromUser = Long.valueOf(userRepository.findByLogin(jwtUtils.extractUsername(jwtUtils.extractToken(request), TokenEnum.ACCESS)).getId());
        List<Message> messagesFrom = (List<Message>) messageRepository.findByFromUser(fromUser);
        List<Message> messagesTo = (List<Message>) messageRepository.findByToUser(fromUser);
        System.out.println(fromUser);
        List<Message> newList = Stream.concat(messagesFrom.stream(), messagesTo.stream()).toList();
        return newList;
    }

    @PostMapping("/upload")
    public void sendMessages(HttpServletRequest request, @RequestBody Message message) {
        User user = userRepository.findByLogin(jwtUtils.extractUsername(jwtUtils.extractToken(request), TokenEnum.ACCESS));
        message.setFromUser(Long.valueOf(user.getId()));
        messageRepository.save(message);
    }
}
