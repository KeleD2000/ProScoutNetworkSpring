package com.example.szakdoga.model;

import lombok.Getter;

@Getter
public class GroupMessage {
    private Integer messageId;
    private Integer senderId;
    private Integer receiverId;
    private String senderUsername;
    private String content;
    private String timestamp;

    public GroupMessage(Integer messageId, Integer senderId, Integer receiverId, String senderUsername, String content, String timestamp) {
        this.messageId = messageId;
        this.content = content;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.timestamp = timestamp;
    }
}

