package com.example.szakdoga.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter //nem kell megirni annotáción keresztül használom
@Setter //nem kell megirni annotáción keresztül használom
@Entity //Entitás lesz
@Table(name = "files") //tábla létrehozása
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer files_id;
    private String type;
    private String format;
    private String file_path;
    @ManyToOne //amit küldök mintami kulcs át lesz adva egy a többnél
    private Player player;

}
