package com.example.szakdoga.model.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScoutRequest {
    private String username;
    private String sport;
    private String team;
    private String email;
    private String first_name;
    private String last_name;
}
