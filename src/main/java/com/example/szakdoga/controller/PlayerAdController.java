package com.example.szakdoga.controller;

import com.example.szakdoga.model.PlayerAd;
import com.example.szakdoga.model.request.PlayerAdRequest;
import com.example.szakdoga.services.PlayerAdService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 0, value = "*")
@RequestMapping("/api")
public class PlayerAdController {

    private final PlayerAdService playerAdService;

    public PlayerAdController(PlayerAdService playerAdService) {
        this.playerAdService = playerAdService;
    }

    @PostMapping(value = "/uploadAds/{username}", consumes = {"multipart/form-data"})
    public PlayerAd uploadAds( @PathVariable("username") String username,@RequestParam("content") String content, @RequestParam("file") MultipartFile file) throws Exception{
        return this.playerAdService.uploadAds(username, content, file);
    }

    @PutMapping("/updateAd/{adId}")
    public PlayerAd updateAd(@PathVariable("adId") Integer adId, @RequestParam("content") String content, @RequestParam("file") MultipartFile file) {
        return playerAdService.updateAd(adId, content, file);
    }

    @DeleteMapping("/deleteAd/{adId}")
    public void deleteAd(@PathVariable("adId") Integer adId) {
        playerAdService.deleteAdById(adId);
    }

    @GetMapping("/playerAds")
    public List<PlayerAd> getAllPlayerAds() {
        return playerAdService.getAllPlayerAds();
    }

    @GetMapping("/adsImage/{playerAdId}")
    public ResponseEntity<byte[]> getPlayerAdImage(@PathVariable Integer playerAdId) {
        byte[] imageBytes = playerAdService.getPlayerAdImageById(playerAdId);

        if (imageBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // vagy MediaType.IMAGE_PNG stb., a kép típusától függően

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
