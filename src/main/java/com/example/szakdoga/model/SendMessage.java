package com.example.szakdoga.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter //nem kell megirni annotáción keresztül használom
@Setter //nem kell megirni annotáción keresztül használom
@Entity //Entitás lesz
@Table //tábla létrehozása
public class SendMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Ettől lesz autoincrement
    private Integer message_id;
    private String message_content;
    @Temporal(TemporalType.DATE)
    private String dateTime;

    private Boolean readed;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    @JsonIgnore
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    @JsonIgnore
    private User receiverUser;
}
