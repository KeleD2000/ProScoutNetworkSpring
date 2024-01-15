package com.example.szakdoga.services;

import com.example.szakdoga.model.*;
import com.example.szakdoga.model.request.*;
import com.example.szakdoga.repository.*;
import exception.InvalidUsernameOrPasswordException;
import exception.PlayerSearchNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
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
    @Autowired
    private FilesRepository filesRepository;

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

    public User updatePlayerProfile(UpdatePlayerRequest updatedPlayerRequest) throws Exception {
        User user = userRepository.findByUsername(updatedPlayerRequest.getUsername())
                .orElseThrow(() -> new Exception("Felhasználó nem található"));

        if (updatedPlayerRequest.getLast_name() != null) {
            user.getPlayer().setLast_name(updatedPlayerRequest.getLast_name());
        }
        if (updatedPlayerRequest.getFirst_name() != null) {
            user.getPlayer().setFirst_name(updatedPlayerRequest.getFirst_name());
        }
       if(updatedPlayerRequest.getEmail() != null){
           user.getPlayer().setEmail(updatedPlayerRequest.getEmail());
       }
       if(updatedPlayerRequest.getLocation() != null){
           user.getPlayer().setLocation(updatedPlayerRequest.getLocation());
       }
       if(updatedPlayerRequest.getSport() != null){
           user.getPlayer().setSport(updatedPlayerRequest.getSport());
       }
       if(updatedPlayerRequest.getAge() != null){
           user.getPlayer().setAge(updatedPlayerRequest.getAge());
       }
       if(updatedPlayerRequest.getPosition() != null){
           user.getPlayer().setPosition(updatedPlayerRequest.getPosition());
       }
        userRepository.save(user);
        return user;
    }

    public User updateScoutProfile(UpdateScoutRequest updateScoutRequest) throws Exception {
        User user = userRepository.findByUsername(updateScoutRequest.getUsername())
                .orElseThrow(() -> new Exception("Felhasználó nem található"));

        if(updateScoutRequest.getTeam() != null){
            user.getScout().setTeam(updateScoutRequest.getTeam());
        }
        if (updateScoutRequest.getLast_name() != null) {
            user.getScout().setLast_name(updateScoutRequest.getLast_name());
        }
        if (updateScoutRequest.getFirst_name() != null) {
            user.getScout().setFirst_name(updateScoutRequest.getFirst_name());
        }
        if(updateScoutRequest.getEmail() != null){
            user.getScout().setEmail(updateScoutRequest.getEmail());
        }
        if(updateScoutRequest.getSport() != null){
            user.getScout().setSport(updateScoutRequest.getSport());
        }
        userRepository.save(user);
        return user;
    }

    public void deletePlayerProfile(String username) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Felhasználó nem található"));

        // Töröld a játékos fájljait, ha vannak
        if (user.getRoles() == Roles.PLAYER) {
            List<File> files = filesRepository.findByUser(user);
            filesRepository.deleteAll(files);
        }

        // Töröld a játékost
        userRepository.delete(user);
    }

    public void deleteScoutProfile(String username) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Felhasználó nem található"));

        // Töröld a játékos fájljait, ha vannak
        if (user.getRoles() == Roles.SCOUT) {
            List<File> files = filesRepository.findByUser(user);
            filesRepository.deleteAll(files);
        }

        // Töröld a játékost
        userRepository.delete(user);
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

    public List<PlayerInfo> getAllPlayersWithUsernames() {
        return playerRepository.findAllPlayersWithUsernames();
    }
    public List<ScoutInfo> getAllScoutsWithUsernames() {
        return scoutRepository.findAllScoutsWithUsernames();
    }

    public List<Player> findPlayersBySearchTerm(String searchTerm) {
        List<Player> players = playerRepository.findByMultipleFields(searchTerm);
        if (players.isEmpty()) {
            throw new PlayerSearchNotFoundException("Nincsen találat erre a keresésre. " + searchTerm);
        }
        return players;
    }

    public List<Scout> findScoutsBySearchTerm(String searchTerm) {
        List<Scout> scouts = scoutRepository.findByMultipleFields(searchTerm);
        if (scouts.isEmpty()) {
            throw new PlayerSearchNotFoundException("Nincsen találat erre a keresésre. " + searchTerm);
        }
        return scouts;
    }

    private final List<User> connectedUsers = new ArrayList<>();
    public void connectUser(String username) throws Exception{
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Felhasználó nem található"));

        if (!connectedUsers.contains(user)) {
            connectedUsers.add(user);
        }
    }

    public List<User> getConnectedUsers() {
        System.out.println(connectedUsers);
        return connectedUsers;
    }
}
