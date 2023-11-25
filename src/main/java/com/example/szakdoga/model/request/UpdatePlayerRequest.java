package com.example.szakdoga.model.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlayerRequest {
    private String username;
    private String last_name;
    private String first_name;
    private Integer age;
    private String sport;
    private String location;
    private String email;
    private String position;
}
