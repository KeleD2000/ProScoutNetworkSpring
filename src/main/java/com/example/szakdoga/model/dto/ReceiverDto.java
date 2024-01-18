package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReceiverDto {
    private Integer receiverUserId;
    private String receiverUsername;

    public ReceiverDto(Integer id, String username) {
        this.receiverUserId = id;
        this.receiverUsername = username;
    }
}
