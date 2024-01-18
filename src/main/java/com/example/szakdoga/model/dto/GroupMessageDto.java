package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class GroupMessageDto {
    private Integer id;
    private String message_content;
    private LocalDateTime timestamp;
    private Integer senderUserId;
    private String senderUsername;
    private boolean groupChat;
    private List<ReceiverDto> receivers;
}
