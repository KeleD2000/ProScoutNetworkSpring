package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ReceiverAllDto {
    private Integer id;
    private String username;
    private String message_content;
}
