package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class ReceiverAllDto {
    private Integer id;
    private String username;
    private String message_content;
    private LocalDateTime timestamp;
}
