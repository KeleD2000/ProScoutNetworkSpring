package com.example.szakdoga.model.dto;

import com.example.szakdoga.model.Roles;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {
    private Integer id;
    private String username;
    private Roles roles;

    public UserDto(Integer id, String username, Roles roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
