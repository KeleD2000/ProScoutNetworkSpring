package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class BidDto {
    private Integer bid_id;
    private String bid_content;
    private Integer offer;
    private LocalDateTime timestamp;
    private Integer senderUserId;
    private String senderUsername;
    private Integer receiverUserId;
    private String receiverUsername;
}
