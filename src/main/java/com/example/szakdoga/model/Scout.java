package com.example.szakdoga.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter //nem kell megirni annotáción keresztül használom
@Setter //nem kell megirni annotáción keresztül használom
@Entity //Entitás lesz
@Table //tábla létrehozása
public class Scout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scout_id;
    private String sport;
    private String team;
    @OneToOne(cascade = CascadeType.ALL) //kapcsolat, cascade szülő-gyerek törlés
    @JoinColumn(name = "user_id") //melyik oszlopot adja hozzá mint idegen kulcs
    private User user;

}
