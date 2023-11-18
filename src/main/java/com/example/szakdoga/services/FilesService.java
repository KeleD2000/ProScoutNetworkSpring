package com.example.szakdoga.services;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.User;
import com.example.szakdoga.model.request.FileRequest;
import com.example.szakdoga.repository.FilesRepository;
import com.example.szakdoga.repository.PlayerRepository;
import com.example.szakdoga.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
}
