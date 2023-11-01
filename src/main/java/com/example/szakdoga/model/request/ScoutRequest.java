package com.example.szakdoga.model.request;

import lombok.*;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoutRequest {
    private String username;
    private String password;
    private String email;
    private String roles;
    private String sport;
    private String first_name;
    private String last_name;
    private String team;
}
