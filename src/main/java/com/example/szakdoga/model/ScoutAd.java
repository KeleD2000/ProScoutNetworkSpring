package com.example.szakdoga.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter //nem kell megirni annotáción keresztül használom
@Setter //nem kell megirni annotáción keresztül használom
@Entity //Entitás lesz
@Table(name = "scout_ad") //tábla létrehozása
public class ScoutAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scoutad_id;
    private String content;
    private String photo_path;
    @ManyToOne //amit küldök mintami kulcs át lesz adva egy a többnél
    @JsonIgnore
    private Scout scout;

    public String getPhotoPathUrl() {
        if (photo_path != null) {
            return "/scoutAds/" + scoutad_id + "/" + photo_path;
        }
        return null;
    }
}
