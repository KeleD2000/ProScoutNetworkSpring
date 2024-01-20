package com.example.szakdoga.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ReportDto {
    private Integer report_id;
    private String report_content;
    private LocalDateTime timestamp;
    private String report_username;
    private Integer senderUserId;
    private String senderUsername;
    private Integer receiverUserId;
    private String receiverUsername;
}
