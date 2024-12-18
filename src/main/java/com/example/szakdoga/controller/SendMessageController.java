package com.example.szakdoga.controller;

import com.example.szakdoga.model.GroupMessages;
import com.example.szakdoga.model.SendMessage;
import com.example.szakdoga.model.dto.MessagesDto;
import com.example.szakdoga.model.dto.ReceiverAllDto;
import com.example.szakdoga.model.dto.ReceiverUserDto;
import com.example.szakdoga.model.dto.SendMessageDto;
import com.example.szakdoga.services.SendMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(value = "*", maxAge = 0)
public class SendMessageController {

    private final SendMessageService sendMessageService;

    public SendMessageController(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @GetMapping("/messages/latest/{receiverUserId}")
    public List<ReceiverAllDto> getLatestMessagesByReceiver(@PathVariable Integer receiverUserId) {
        return sendMessageService.getAllMessagesForReceiver(receiverUserId);
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<SendMessage>> getConversation(
            @RequestParam Integer senderId,
            @RequestParam Integer receiverId) {
        List<SendMessage> messages = sendMessageService.getConversation(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/groupChatMessages/{userId1}/{userId2}")
    public ResponseEntity<List<GroupMessages>> getGroupChatMessages(@PathVariable Integer userId1, @PathVariable Integer userId2) {
        List<GroupMessages> groupedMessages = sendMessageService.getGroupChatMessages(userId1, userId2);
        return new ResponseEntity<>(groupedMessages, HttpStatus.OK);
    }

}
