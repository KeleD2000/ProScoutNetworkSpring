package com.example.szakdoga.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Ettől lesz autoincrement
    private Integer report_id;
    private String report_content;
    private String report_username;
    private LocalDateTime timestamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User senderUser;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private User receiverUser;
}
