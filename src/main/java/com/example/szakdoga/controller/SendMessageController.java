package com.example.szakdoga.controller;

import com.example.szakdoga.model.dto.SendMessageDto;
import com.example.szakdoga.services.SendMessageService;
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

    @GetMapping("/message/{senderUserId}/{receiverUserId}")
    public List<SendMessageDto> getMessages(@PathVariable Integer senderUserId, @PathVariable Integer receiverUserId){
        return sendMessageService.getAllMessage(senderUserId, receiverUserId);
    }
}
