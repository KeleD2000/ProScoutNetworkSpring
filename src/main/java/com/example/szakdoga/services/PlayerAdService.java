package com.example.szakdoga.services;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.PlayerAd;
import com.example.szakdoga.model.User;
import com.example.szakdoga.model.request.PlayerAdRequest;
import com.example.szakdoga.repository.PlayerAdRepository;
import com.example.szakdoga.repository.PlayerRepository;
import com.example.szakdoga.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerAdService {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PlayerAdRepository playerAdRepository;


    private static final String UPLOAD_DIR = "src/adsPhotos/";

    public PlayerAd updateAd(Integer adId,  String content, MultipartFile file) {
        // Szöveg módosítása
        Optional<PlayerAd> existingAd = playerAdRepository.findById(adId);
        if (existingAd.isPresent()) {
            PlayerAd playerAd = existingAd.get();
            playerAd.setContent(content);

            // Kép módosítása, ha új kép van feltöltve
            if (file != null && !file.isEmpty()) {
                try {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    String filePath = UPLOAD_DIR + fileName;

                    // Fájl mentése
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(filePath);
                    Files.write(path, bytes);

                    // Régi fájl törlése, ha volt
                    if (playerAd.getPhoto_path() != null) {
                        Path oldPath = Paths.get(playerAd.getPhoto_path());
                        Files.delete(oldPath);
                    }

                    // Új fájl elérési út beállítása
                    playerAd.setPhoto_path(String.valueOf(path));
                } catch (IOException e) {
                    throw new RuntimeException("Hiba történt a kép mentése közben", e);
                }
            }

            // PlayerAd mentése
            return playerAdRepository.save(playerAd);
        } else {
            throw new RuntimeException("Az hirdetés nem található az azonosító alapján: " + adId);
        }
    }

    public PlayerAd uploadAds(String username, String content, MultipartFile file){
        if (file.isEmpty()) {
            throw new RuntimeException("Hiba");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String filePath = UPLOAD_DIR + fileName;


        try {
            // Fájl mentése
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);

            // Player és PlayerAd létrehozása
            Optional<User> user = userRepository.findByUsername(username);
            Optional<Player> player = playerRepository.findByUserId(user.get().getId());

            PlayerAd playerAd = new PlayerAd();
            playerAd.setContent(content);
            playerAd.setPhoto_path(String.valueOf(path));
            playerAd.setPlayer(player.orElse(null)); // Player objektum vagy null, ha nincs megtalálva

            playerAdRepository.save(playerAd);

            return playerAd;
        } catch (Exception e) {
            throw new RuntimeException("Hiba történt a fájl mentése közben", e);
        }
    }

    public void deleteAdById(Integer adId) {
        Optional<PlayerAd> existingAd = playerAdRepository.findById(adId);
        if (existingAd.isPresent()) {
            PlayerAd playerAd = existingAd.get();

            // Kép törlése, ha van
            if (playerAd.getPhoto_path() != null) {
                try {
                    Path imagePath = Paths.get(playerAd.getPhoto_path());
                    Files.delete(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Hiba történt a kép törlése közben", e);
                }
            }

            // Hirdetés törlése az adatbázisból
            playerAdRepository.deleteById(adId);
        } else {
            throw new RuntimeException("Az hirdetés nem található az azonosító alapján: " + adId);
        }
    }

    public List<PlayerAd> getAllPlayerAds() {
        return playerAdRepository.findAll();
    }
}
