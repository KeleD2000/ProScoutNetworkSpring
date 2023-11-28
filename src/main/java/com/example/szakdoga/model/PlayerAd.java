package com.example.szakdoga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter //nem kell megirni annotáción keresztül használom
@Setter //nem kell megirni annotáción keresztül használom
@Entity //Entitás lesz
@Table(name = "player_ad") //tábla létrehozása
public class PlayerAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playerad_id;
    private String content;
    private String photo_path;
    @ManyToOne //amit küldök mintami kulcs át lesz adva egy a többnél
    @JsonIgnore //lekérem a fájlt, akkor nem irja ki a player adatait
    private Player player;

}
