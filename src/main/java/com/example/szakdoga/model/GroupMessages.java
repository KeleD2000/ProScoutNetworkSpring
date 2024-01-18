package com.example.szakdoga.model;

import lombok.Getter;

import java.util.List;

@Getter
public class GroupMessages {
    private String receiverUsername;
    private List<GroupMessage> messages;

    public GroupMessages(String senderUsername, List<GroupMessage> messages) {
        this.receiverUsername = senderUsername;
        this.messages = messages;
    }


}
