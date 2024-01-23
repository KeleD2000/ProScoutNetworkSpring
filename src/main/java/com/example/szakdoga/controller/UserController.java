package com.example.szakdoga.controller;

import com.example.szakdoga.model.*;
import com.example.szakdoga.model.dto.UserDto;
import com.example.szakdoga.model.request.*;
import com.example.szakdoga.repository.UserRepository;
import com.example.szakdoga.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(maxAge = 0, value = "*")
@RequestMapping("/api")
public class UserController {


    private final UserService userService;

    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
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

    @PatchMapping("/update_scout")
    public ResponseEntity<?> updateScout(@RequestBody UpdateScoutRequest updateScoutRequest) {
        try {
            User updatedUser = userService.updateScoutProfile(updateScoutRequest);
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

    @DeleteMapping("/delete_scout_profile/{username}")
    public ResponseEntity<?> deleteScoutProfile(@PathVariable String username) {
        try {
            userService.deleteScoutProfile(username);
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
    public ResponseEntity<List<PlayerInfo>> getAllPlayersWithUsernames() {
        List<PlayerInfo> players = userService.getAllPlayersWithUsernames();
        return ResponseEntity.ok(players);
    }
    @GetMapping("/scouts")
    public ResponseEntity<List<ScoutInfo>> getAllWithUsernames() {
        List<ScoutInfo> scouts = userService.getAllScoutsWithUsernames();
        return ResponseEntity.ok(scouts);
    }

    @GetMapping("/search-scout")
    public ResponseEntity<List<Player>> searchPlayers(@RequestParam String searchTerm) {
        List<Player> players = userService.findPlayersBySearchTerm(searchTerm);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/search-player")
    public ResponseEntity<List<Scout>> searchScout(@RequestParam String searchTerm) {
        List<Scout> scouts = userService.findScoutsBySearchTerm(searchTerm);
        return ResponseEntity.ok(scouts);
    }

    @PostMapping("/bid/connect/{username}")
    public ResponseEntity<Map<String, String>> connectUser(@PathVariable String username) {
        try {
            userService.connectUser(username);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Felhasználó sikeresen csatlakoztatva");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDTOs = users.stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(),user.getRoles()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/bid/connected")
    public List<User> getConnectedUsers() {
        return userService.getConnectedUsers();
    }

    @GetMapping("/avgAdCount")
    public Object getAverageAdCount() {
        return userService.calculateAverageAdCount();
    }

    @GetMapping("/averagePercentageBySport")
    public ResponseEntity<List<Object[]>> getAveragePercentageBySport() {
        List<Object[]> results = userService.getAveragePercentageBySport();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/averagePercentageBySportScout")
    public ResponseEntity<List<Object[]>> getAveragePercentageBySportScout() {
        List<Object[]> results = userService.getAveragePercentageBySportScout();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/topPlayerBySports")
    public ResponseEntity<List<Object[]>> getTopPlayerBySport() {
        List<Object[]> results = userService.getTopPlayerBySport();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/topScoutBySports")
    public ResponseEntity<List<Object[]>> getTopScoutBySport() {
        List<Object[]> results = userService.getTopScoutBySport();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

}


