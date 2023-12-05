package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SendMessageDto {
    private Integer id;
    private String message_content;
    private String dateTime;
    private boolean readed;
    private Integer senderUserId;
    private String senderUserFirstName;
    private String senderUserLastName;
    private Integer receiverUserId;
    private String receiverUserFirstName;
    private String receiverUserLastName;
    private String senderUserProfilePicture;
    private String receiverUserProfilePicture;
}
