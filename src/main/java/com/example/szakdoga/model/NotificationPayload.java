package com.example.szakdoga.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationPayload {
    private String username;
    private String message;
    private Integer senderId;
    private String senderUsername;
}
