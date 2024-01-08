package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class SendMessageDto {
    private Integer id;
    private String message_content;
    private LocalDateTime timestamp;
    private boolean readed;
    private Integer senderUserId;
    private String senderUsername;
    private Integer receiverUserId;
    private String receiverUsername;
}
