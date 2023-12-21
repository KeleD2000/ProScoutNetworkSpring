package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class MessagesDto {
    private Integer message_id;
    private Date date_time;
    private String message_content;
    private Boolean readed;
    private Integer receiver_user_id;
    private Integer sender_user_id;
}
