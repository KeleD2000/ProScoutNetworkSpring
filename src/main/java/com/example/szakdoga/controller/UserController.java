package com.example.szakdoga.controller;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.User;
import com.example.szakdoga.model.request.FileRequest;
import com.example.szakdoga.model.request.LoginRequest;
import com.example.szakdoga.model.request.PlayerRequest;
import com.example.szakdoga.model.request.ScoutRequest;
import com.example.szakdoga.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public UserDetails login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }


    @GetMapping("/current-user/{username}")
    public User getCurrentUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}


