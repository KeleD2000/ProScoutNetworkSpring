package com.example.szakdoga.controller;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.request.FileRequest;
import com.example.szakdoga.services.FilesService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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

    @PostMapping("/uploadVideo")
    public File handleVideoFile(@RequestParam("type") String type, @RequestParam("format") String format, @RequestParam("username") String username, @RequestParam("file") MultipartFile file) {
        return  filesService.handleVideoFile(type, format, username, file);
    }

    @PostMapping("/uploadPdf")
    public File handlePdfUpload(@RequestParam("type") String type, @RequestParam("format") String format, @RequestParam("username") String username,  @RequestParam("file") @RequestPart(value = "file", required = true) MultipartFile file) {
        return filesService.handlePdfUpload(type, format, username, file);
    }

    @GetMapping("/downloadPdf/{fileId}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long fileId) throws IOException {
        // Letölti a PDF fájlt a PdfDownloadService segítségével
        byte[] contents = filesService.downloadPdf(fileId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // Ezt a részt módosíthatod az adott fájl nevére
        headers.setContentDispositionFormData("attachment", "filename.pdf");

        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }


    @GetMapping("/profilkep/{username}")
    public ResponseEntity<byte[]> getProfilePic(@PathVariable String username) {
        byte[] imageBytes = filesService.getProfilePic(username);

        if (imageBytes != null) {
            return ResponseEntity.ok().body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteProfilePic/{username}")
    public ResponseEntity<Void> deleteProfilePic(@PathVariable String username) {
        filesService.deleteProfilePic(username);
        return ResponseEntity.noContent().build();
    }
}
