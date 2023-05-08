package jg.actionfigures.server.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jg.actionfigures.server.API.CassandraChatRepository;
import jg.actionfigures.server.API.CassandraMessageRepository;
import jg.actionfigures.server.API.MessageSender;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.AuthModule.Utils.JwtUtils;
import jg.actionfigures.server.AuthModule.Utils.TokenEnum;
import jg.actionfigures.server.Models.Cassandra.Chat;
import jg.actionfigures.server.Models.Cassandra.Message;
import jg.actionfigures.server.Models.PostgerSql.User;

@RestController
@RequestMapping("/test")
public class MainController {

    @Autowired
    AmqpTemplate template;

    @Autowired
    CassandraMessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CassandraChatRepository chatRepository;

    @Autowired
    MessageSender messageSender;

    public void sendEmailToUser(String login, String emailMessage) {
        messageSender.sendMessageToUser(login, emailMessage);
        
    }

    private JwtUtils jwtUtils = new JwtUtils();

    @GetMapping("/xd")
    public String test() {
        return "тест";
    }

    @PostMapping("/messages")
    public List<Message> getMessages(@RequestBody Long chatRoomId) {
        List<Message> messages = (List<Message>) messageRepository.findAllByChatRoomId(chatRoomId);
        System.out.println(chatRoomId);
        System.out.println(messages);
        return messages;
    }

    @GetMapping("/chats")
    public List<Chat> getChats(HttpServletRequest request) {
        List<Chat> chats = chatRepository.findByUser(jwtUtils.extractUsername(jwtUtils.extractToken(request), TokenEnum.ACCESS));
        System.out.println(chats);
        List<Chat> output = new ArrayList<Chat>();
        for(int i = 0; i < chats.size(); i++) {
            List<Chat> temp = chatRepository.findByChatRoomId(chats.get(i).getChatRoomId());
            for(int j = 0; j < temp.size(); j++) {
                if(!temp.get(j).getUser().equals(jwtUtils.extractUsername(jwtUtils.extractToken(request), TokenEnum.ACCESS))) {
                    output.add(temp.get(j));
                }
            }
        }
        System.out.println(output);
        return output;
    }

    @PostMapping("/upload")
    public void sendMessages(HttpServletRequest request, @RequestBody Message message) {
        message.setFromUser(jwtUtils.extractUsername(jwtUtils.extractToken(request), TokenEnum.ACCESS));
        System.out.println(message.getContent());
        messageRepository.save(message);
        sendEmailToUser(message.getToUser(), message.getContent());
    }


    @GetMapping("/me")
    public User getUser(HttpServletRequest request) {
        User me = new User();
        me = userRepository.findByLogin(jwtUtils.extractUsername(jwtUtils.extractToken(request), TokenEnum.ACCESS));
        me.setPassword("");
        return me; 
    }

    @PostMapping("/user")
    public User getUserById(@RequestBody Integer id) {
        User user = new User();
        user = userRepository.findById(id).get();
        user.setPassword("");
        return user;
    }

    @GetMapping("/new-chat-with")
    public List<String> postMethodName(HttpServletRequest request) {
        List<Chat> tempChats = getChats(request);
        Chat myself = new Chat();
        myself.setUser(jwtUtils.extractUsername(jwtUtils.extractToken(request), TokenEnum.ACCESS));
        tempChats.add(myself);
        List<User> allUsers = (List<User>) userRepository.findAll();
        List<String> answer = new ArrayList<String>();
        boolean tempK = false;
        for(int i = 0; i < allUsers.size(); i++) {
            for(int j = 0; j < tempChats.size(); j++) {
                if(allUsers.get(i).getLogin().equals(tempChats.get(j).getUser())) {
                    tempK = true;
                }
            }
            if(!tempK) {
                answer.add(allUsers.get(i).getLogin());
            }
            tempK = false;
        }
        System.out.println(answer);
        return answer;
    }
    
}
