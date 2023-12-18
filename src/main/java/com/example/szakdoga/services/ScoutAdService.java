package com.example.szakdoga.services;

import com.example.szakdoga.model.*;
import com.example.szakdoga.repository.*;
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
public class ScoutAdService {

    @Autowired
    ScoutRepository scoutRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ScoutAdRepository scoutAdRepository;

    private static final String UPLOAD_DIR = "src/scoutAds/";

    public ScoutAd updateAd(Integer adId, String content, MultipartFile file) {
        // Szöveg módosítása
        Optional<ScoutAd> existingAd = scoutAdRepository.findById(adId);
        if (existingAd.isPresent()) {
            ScoutAd scoutAd = existingAd.get();
            scoutAd.setContent(content);

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
                    if (scoutAd.getPhoto_path() != null) {
                        Path oldPath = Paths.get(scoutAd.getPhoto_path());
                        Files.delete(oldPath);
                    }

                    // Új fájl elérési út beállítása
                    scoutAd.setPhoto_path(String.valueOf(path));
                } catch (IOException e) {
                    throw new RuntimeException("Hiba történt a kép mentése közben", e);
                }
            }

            // PlayerAd mentése
            return scoutAdRepository.save(scoutAd);
        } else {
            throw new RuntimeException("Az hirdetés nem található az azonosító alapján: " + adId);
        }
    }

    public ScoutAd uploadAds(String username, String content, MultipartFile file){
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
            Optional<Scout> scout = scoutRepository.findByUserId(user.get().getId());

            ScoutAd scoutAd = new ScoutAd();
            scoutAd.setContent(content);
            scoutAd.setPhoto_path(String.valueOf(path));
            scoutAd.setScout(scout.orElse(null)); // Player objektum vagy null, ha nincs megtalálva

            scoutAdRepository.save(scoutAd);

            return scoutAd;
        } catch (Exception e) {
            throw new RuntimeException("Hiba történt a fájl mentése közben", e);
        }
    }

    public void deleteAdById(Integer adId) {
        Optional<ScoutAd> existingAd = scoutAdRepository.findById(adId);
        if (existingAd.isPresent()) {
            ScoutAd scoutAd = existingAd.get();

            // Kép törlése, ha van
            if (scoutAd.getPhoto_path() != null) {
                try {
                    Path imagePath = Paths.get(scoutAd.getPhoto_path());
                    Files.delete(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Hiba történt a kép törlése közben", e);
                }
            }

            // Hirdetés törlése az adatbázisból
            scoutAdRepository.deleteById(adId);
        } else {
            throw new RuntimeException("Az hirdetés nem található az azonosító alapján: " + adId);
        }
    }

    public List<ScoutAd> getAllScoutAds() {
        return scoutAdRepository.findAll();
    }


    public byte[] getScoutAdImageById(Integer scoutAdId) {
        Optional<ScoutAd> scoutAd = scoutAdRepository.findById(scoutAdId);

        if (scoutAd.isPresent()) {
            String photoPath = scoutAd.get().getPhoto_path();
            if (photoPath != null) {
                Path imagePath = Paths.get(photoPath);
                try {
                    if (Files.exists(imagePath)) {
                        return Files.readAllBytes(imagePath);
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Hiba a kép lekérése közben", e);
                }
            }
        }

        return null;
    }
}
