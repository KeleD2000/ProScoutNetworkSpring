package com.example.szakdoga.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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
    @OneToMany(mappedBy = "user",  fetch = FetchType.EAGER) //így adom át egy a több kapcsolatnál a kulcsot
    private List<File> files; //ez is kell
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
