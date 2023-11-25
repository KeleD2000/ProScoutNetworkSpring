package com.example.szakdoga.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter //nem kell megirni annotáción keresztül használom
@Setter //nem kell megirni annotáción keresztül használom
@Entity //Entitás lesz
@Table //tábla létrehozása
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Ettől lesz autoincrement
    private Integer id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING) //
    private Roles roles;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private Player player;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Scout scout;
}
