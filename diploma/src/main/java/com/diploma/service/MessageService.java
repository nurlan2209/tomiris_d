package com.diploma.service;

import com.diploma.model.Message;
import com.diploma.model.User;
import com.diploma.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    private final UserService userService;


    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public Message createMessage(Long userId, String userMessage, String botMessage) {
        Message message = new Message();
        message.setUserMessage(userMessage);
        message.setUser(userService.getUserById(userId));
        message.setBotMessage(botMessage);
        return messageRepository.save(message);
    }

    public List<Message> getAllMessageByUserId(Long userId) {
        User user = userService.getUserById(userId);
        return messageRepository.findByUser(user);
    }
}
