package jg.actionfigures.server.AuthModule.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jg.actionfigures.server.API.MessageSender;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.Models.PostgerSql.User;

@Service
public class UserService {

    private UserRepository userRepository;
    private MessageSender messageSender;

    public UserService(UserRepository userRepository, MessageSender messageSender) {
        this.userRepository = userRepository;
        this.messageSender = messageSender;
    }

    public void sendEmailToUser(String login, String emailMessage) {
        messageSender.sendMessageToUser(login, emailMessage);
    }

}
