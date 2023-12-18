package com.example.szakdoga.controller;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.Scout;
import com.example.szakdoga.model.User;
import com.example.szakdoga.model.request.*;
import com.example.szakdoga.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 0, value = "*")
@RequestMapping("/api")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register_scout")
    public User registerScout(@RequestBody ScoutRequest scoutRequest) throws Exception {
        System.out.println(scoutRequest);
        return userService.registerScout(scoutRequest);
    }

    @PostMapping("/register_player")
    public User registerPlayer(@RequestBody PlayerRequest playerRequest) throws Exception {
        System.out.println(playerRequest);
        return userService.registerPlayer(playerRequest);
    }

    @PatchMapping("/update_profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdatePlayerRequest updatedPlayerRequest) {
        try {
            User updatedUser = userService.updatePlayerProfile(updatedPlayerRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete_player_profile/{username}")
    public ResponseEntity<?> deletePlayerProfile(@PathVariable String username) {
        try {
            userService.deletePlayerProfile(username);
            return ResponseEntity.ok("Profil sikeresen törölve");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public UserDetails login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }


    @GetMapping("/current-user/{username}")
    public User getCurrentUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = userService.getAllPlayers();
        return ResponseEntity.ok(players);
    }
    @GetMapping("/scouts")
    public ResponseEntity<List<Scout>> getAllScouts() {
        List<Scout> scout = userService.getAllScout();
        return ResponseEntity.ok(scout);
    }

}


