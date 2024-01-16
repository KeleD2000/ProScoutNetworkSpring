package com.example.szakdoga.controller;

import com.example.szakdoga.model.NotificationPayload;
import com.example.szakdoga.model.SendMessage;
import com.example.szakdoga.model.User;
import com.example.szakdoga.model.dto.SendMessageDto;
import com.example.szakdoga.repository.SendMessageRepository;
import com.example.szakdoga.repository.UserRepository;
import com.example.szakdoga.services.UserService;
import exception.UserNotFoundException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {
    private final SendMessageRepository sendMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserService userService;

    public WebSocketController(SendMessageRepository sendMessageRepository, UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate, UserService userService) {
        this.sendMessageRepository = sendMessageRepository;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }

    @MessageMapping("/notify/{username}")
    public void sendNotification(
            @DestinationVariable String username,
            @Payload NotificationPayload payload) {

        // Küldő felhasználó adatainak lekérése a Principal objektumból
        String senderUsername = payload.getSenderUsername();
        Integer senderId = payload.getSenderId();

        // Az üzenet adatainak tárolása Map objektumban
        Map<String, Object> notification = new HashMap<>();
        notification.put("username", username);
        notification.put("message", payload.getMessage());
        notification.put("senderId", senderId);  // A küldő felhasználó id-ja
        notification.put("senderUsername", senderUsername);  // A küldő felhasználó username-je

        // Az üzenet elküldése a megfelelő címre
        simpMessagingTemplate.convertAndSend("/queue/notify/" + username, notification);
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
        saveSendMessage(chatMessage, sender, receiver);

        // Üzenet küldése a megfelelő címzettnek (receiverUserId)
        simpMessagingTemplate.convertAndSend("/queue/private/" + receiver.getUsername(), chatMessage);
    }

    @MessageMapping("/chat.sendPrivateMessageToUsers/{userId1}/{userId2}")
    public void sendPrivateMessageToUsers(@DestinationVariable Integer userId1,
                                          @DestinationVariable Integer userId2,
                                          @Payload SendMessageDto chatMessage) {
        User sender = userRepository.findById(chatMessage.getSenderUserId()).orElse(null);
        User user1 = userRepository.findById(userId1).orElse(null);
        User user2 = userRepository.findById(userId2).orElse(null);

        if (user1 == null || user2 == null) {
            throw new UserNotFoundException("Az egyik vagy mindkét felhasználó nem található!");
        }

        // Mentés az adatbázisba
        saveSendMessage(chatMessage, sender, user1);
        saveSendMessage(chatMessage, sender, user2);
        // Üzenet küldése mindkét címzettnek
        simpMessagingTemplate.convertAndSend("/queue/group/" + user1.getUsername(), chatMessage);
        simpMessagingTemplate.convertAndSend("/queue/group/" + user2.getUsername(), chatMessage);
    }

    private void saveSendMessage(@Payload SendMessageDto chatMessage, User sender, User user1) {
        SendMessage entity = new SendMessage();
        entity.setSenderUser(sender);
        entity.setReceiverUser(user1);
        entity.setMessage_content(chatMessage.getMessage_content());
        entity.setTimestamp(LocalDateTime.now());
        SendMessage sendMessage = sendMessageRepository.save(entity);
        chatMessage.setId(sendMessage.getMessage_id());
    }

}
