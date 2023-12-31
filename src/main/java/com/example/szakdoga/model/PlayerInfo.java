package com.example.szakdoga.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerInfo {
    private Player player;
    private String username;

    public PlayerInfo(Player player, String username) {
        this.player = player;
        this.username = username;
    }
}
