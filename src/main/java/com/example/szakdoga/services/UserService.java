package com.example.szakdoga.services;

import com.example.szakdoga.model.*;
import com.example.szakdoga.model.request.FileRequest;
import com.example.szakdoga.model.request.LoginRequest;
import com.example.szakdoga.model.request.PlayerRequest;
import com.example.szakdoga.model.request.ScoutRequest;
import com.example.szakdoga.repository.*;
import exception.InvalidUsernameOrPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ScoutRepository scoutRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;




    public User registerScout(ScoutRequest scoutRequest) throws Exception{
        if(userRepository.findByUsername(scoutRequest.getUsername()).isPresent()){
            throw new Exception("Ugyanaz a felhasználónév");
        }
        User newUser = new User();
        newUser.setUsername(scoutRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(scoutRequest.getPassword()));
        if(scoutRequest.getRoles().equals(Roles.SCOUT.name())) {
            newUser.setRoles(Roles.SCOUT);
            Scout scout = new Scout();
            scout.setUser(newUser);
            scout.setLast_name(scoutRequest.getLast_name());
            scout.setFirst_name(scoutRequest.getFirst_name());
            scout.setSport(scoutRequest.getSport());
            scout.setTeam(scoutRequest.getTeam());
            scout.setEmail(scoutRequest.getEmail());
            scoutRepository.save(scout);
        }else{
            newUser.setRoles(Roles.ADMIN);
            Admin admin = new Admin();
            admin.setUser(newUser);
            adminRepository.save(admin);
        }
        userRepository.save(newUser);
        return newUser;
    }


    public User registerPlayer(PlayerRequest playerRequest) throws Exception {
        if(userRepository.findByUsername(playerRequest.getUsername()).isPresent()){
            throw new Exception("Ugyanaz a felhasználónév");
        }
        User newUser = new User();
        newUser.setUsername(playerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(playerRequest.getPassword()));
        if(playerRequest.getRoles().equals(Roles.PLAYER.name())){
            newUser.setRoles(Roles.PLAYER);
            Player player = new Player();
            player.setUser(newUser);
            player.setLast_name(playerRequest.getLast_name());
            player.setFirst_name(playerRequest.getFirst_name());
            player.setAge(playerRequest.getAge());
            player.setSport(playerRequest.getSport());
            player.setLocation(playerRequest.getLocation());
            player.setEmail(playerRequest.getEmail());
            player.setPosition(playerRequest.getPosition());
            playerRepository.save(player);
        }else{
            newUser.setRoles(Roles.ADMIN);
            Admin admin = new Admin();
            admin.setUser(newUser);
            adminRepository.save(admin);
        }
        userRepository.save(newUser);
        return newUser;
    }

    public UserDetails login(LoginRequest loginRequest) {
        try {
            Authentication userDetails = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            return (UserDetails) userDetails.getPrincipal();
        } catch (AuthenticationException authe) {
            throw new InvalidUsernameOrPasswordException("Hibás felhasználónév vagy jelszó.");
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Felhasználó nem található: " + username));
    }

}
