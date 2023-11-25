package com.example.szakdoga.services;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.User;
import com.example.szakdoga.model.request.FileRequest;
import com.example.szakdoga.repository.FilesRepository;
import com.example.szakdoga.repository.PlayerRepository;
import com.example.szakdoga.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class FilesService {
    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UserRepository userRepository;
    public File handleFileUpload(String type, String format, String username, MultipartFile file) {
        // Ellenőrizd, hogy a fájl ne legyen üres
        if (file.isEmpty()) {
            throw new RuntimeException("Hiba");
        }

        Optional<User> user = userRepository.findByUsername(username);
        Optional<Player> player = playerRepository.findByUserId(user.get().getId());
        try {
            for (File f : player.get().getFiles()) {
                if (f.getType().equals("profilpic")) {
                    Path path = Paths.get("src/profilePic/" + f.getFile_path());
                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                }

            }
            // Fájl nevének generálása
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Fájl mentése
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/profilePic/" + fileName);
            Files.write(path, bytes);
            // Entitás létrehozása és mentése az adatbázisban
            File files = new File();
            files.setFormat(format);
            files.setType(type);
            files.setFile_path(String.valueOf(path));
            files.setPlayer(player.get());
            filesRepository.save(files);
            return files;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    public File handleVideoFile(String type, String format, String username, MultipartFile file) {
        // Ellenőrizd, hogy a fájl ne legyen üres
        if (file.isEmpty()) {
            throw new RuntimeException("Hiba");
        }

        Optional<User> user = userRepository.findByUsername(username);
        Optional<Player> player = playerRepository.findByUserId(user.get().getId());
        try {
            for (File f : player.get().getFiles()) {
                if (f.getType().equals("video")) {
                    Path path = Paths.get("src/video/" + f.getFile_path());
                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                }

            }
            // Fájl nevének generálása
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Fájl mentése
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/video/" + fileName);
            Files.write(path, bytes);
            // Entitás létrehozása és mentése az adatbázisban
            File files = new File();
            files.setFormat(format);
            files.setType(type);
            files.setFile_path(String.valueOf(path));
            files.setPlayer(player.get());
            filesRepository.save(files);
            return files;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public File handlePdfUpload(String type, String format, String username, MultipartFile file){
        if (file.isEmpty()) {
            throw new RuntimeException("Hiba");
        }
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Player> player = playerRepository.findByUserId(user.get().getId());
        System.out.println(player);
        try {
            for (File f : player.get().getFiles()) {
                if (f.getType().equals("pdf")) {
                    Path path = Paths.get("src/pdf/" + f.getFile_path());
                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                }

            }
            // Fájl nevének generálása
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Fájl mentése
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/pdf/" + fileName);
            Files.write(path, bytes);
            // Entitás létrehozása és mentése az adatbázisban
            File files = new File();
            files.setFormat(format);
            files.setType(type);
            files.setFile_path(String.valueOf(path));
            files.setPlayer(player.get());
            filesRepository.save(files);
            return files;
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public byte[] downloadPdf(Long fileId) throws IOException {
        // Először lekéred a fájl elérési útvonalát az adatbázisból fileId alapján
        File file = filesRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("A fájl nem található"));

        Path path = Paths.get(file.getFile_path());

        // A fájl tartalmának beolvasása byte tömbbe
        return Files.readAllBytes(path);
    }

    public String getVideoUrl(Long fileId) {
        Optional<File> optionalFile = filesRepository.findById(fileId);
        if (optionalFile.isPresent()) {
            return "/api/videos/" + fileId + "/download";
        } else {
            throw new RuntimeException("A fájl nem található");
        }
    }

    public byte[] downloadVideo(Long fileId) {
        Optional<File> optionalFile = filesRepository.findById(fileId);
        if (optionalFile.isPresent()) {
            File file = optionalFile.get();
            try {
                Path path = Paths.get(file.getFile_path());
                return Files.readAllBytes(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Hiba a videó letöltése közben");
            }
        } else {
            throw new RuntimeException("A fájl nem található");
        }
    }

    public byte[] getProfilePic(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Player> player = playerRepository.findByUserId(user.get().getId());

        for (File f : player.get().getFiles()) {
            if (f.getType().equals("profilpic")) {
                Path imagePath = Paths.get(f.getFile_path());
                System.out.println(imagePath);

                try {
                    if (Files.exists(imagePath)) {
                        return Files.readAllBytes(imagePath);
                    } else {
                        // Ha a kép nem létezik, visszaadjunk null-t vagy egy alapértelmezett képet
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Hiba a profilkép lekérése közben", e);
                }
            }
        }

        // Ha a felhasználónak nincs profilképe, visszaadjunk null-t vagy egy alapértelmezett képet
        return null;
    }

    public void deleteProfilePic(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Player> player = playerRepository.findByUserId(user.get().getId());

        for (File f : player.get().getFiles()) {
            if (f.getType().equals("profilpic")) {
                Path imagePath = Paths.get(f.getFile_path());

                try {
                    if (Files.exists(imagePath)) {
                        Files.delete(imagePath);
                        // Töröld az entitást az adatbázisból
                        filesRepository.delete(f);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Hiba a profilkép törlése közben", e);
                }
            }
        }
    }
}
