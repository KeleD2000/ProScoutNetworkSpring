package com.example.szakdoga.model;

import com.example.szakdoga.model.Scout;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoutInfo {
    private Scout scout;
    private String username;

    public ScoutInfo(Scout scout, String username) {
        this.scout = scout;
        this.username = username;
    }
}
