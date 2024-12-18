package com.example.szakdoga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter //nem kell megirni annotáción keresztül használom
@Setter //nem kell megirni annotáción keresztül használom
@Entity //Entitás lesz
@Table(name = "player") //tábla létrehozása

public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
    private String email;
    private String sport;
    private String location;
    private String position;
    private Integer age;
    private String first_name;
    private String last_name;
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private List<PlayerAd>playerAds;

}
