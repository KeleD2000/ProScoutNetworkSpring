package com.example.szakdoga.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter //nem kell megirni annotáción keresztül használom
@Setter //nem kell megirni annotáción keresztül használom
@Entity //Entitás lesz
@Table(name = "scout") //tábla létrehozása
public class Scout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String sport;
    private String team;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
