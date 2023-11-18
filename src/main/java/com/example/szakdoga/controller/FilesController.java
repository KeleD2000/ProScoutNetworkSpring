package com.example.szakdoga.controller;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.request.FileRequest;
import com.example.szakdoga.services.FilesService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(maxAge = 0, value = "*")
@RequestMapping("/api")
public class FilesController {

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostMapping("/upload")
    public File handleFileUpload(@RequestParam("type") String type, @RequestParam("format") String format, @RequestParam("username") String username, @RequestParam("file") MultipartFile file) {
        return  filesService.handleFileUpload(type, format, username, file);
    }
}
