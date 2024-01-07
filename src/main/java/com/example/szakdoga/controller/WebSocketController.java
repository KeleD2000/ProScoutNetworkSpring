package com.example.szakdoga.controller;

import com.example.szakdoga.model.SendMessage;
import com.example.szakdoga.model.User;
import com.example.szakdoga.model.dto.SendMessageDto;
import com.example.szakdoga.repository.SendMessageRepository;
import com.example.szakdoga.repository.UserRepository;
import exception.UserNotFoundException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final SendMessageRepository sendMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketController(SendMessageRepository sendMessageRepository, UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.sendMessageRepository = sendMessageRepository;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat.sendPrivateMessage/{receiverId}")
    public void sendPrivateMessage(@DestinationVariable Integer receiverId, @Payload SendMessageDto chatMessage) {
        User sender = userRepository.findById(chatMessage.getSenderUserId()).orElse(null);
        User receiver = userRepository.findById(chatMessage.getReceiverUserId()).orElse(null);

        if (sender == null) {
            throw new UserNotFoundException("A küldő felhasználó nem található!");
        }
        if (receiver == null) {
            throw new UserNotFoundException("A fogadó felhasználó nem található!");
        }
        // Mentés az adatbázisba
        SendMessage entity = new SendMessage();
        entity.setSenderUser(sender);
        entity.setReceiverUser(receiver);
        entity.setMessage_content(chatMessage.getMessage_content());
        entity.setDateTime(chatMessage.getDateTime());
        entity.setReaded(chatMessage.isReaded());
        SendMessage sendMessage = sendMessageRepository.save(entity);
        chatMessage.setId(sendMessage.getMessage_id());

        // Üzenet küldése a megfelelő címzettnek (receiverUserId)
        simpMessagingTemplate.convertAndSend("/queue/private/" + receiver.getUsername(), chatMessage);
    }
}
