package com.example.szakdoga.controller;

import com.example.szakdoga.model.PlayerAd;
import com.example.szakdoga.model.Scout;
import com.example.szakdoga.model.ScoutAd;
import com.example.szakdoga.services.ScoutAdService;
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
public class ScoutAdController {

    private final ScoutAdService scoutAdService;

    public ScoutAdController(ScoutAdService scoutAdService) {
        this.scoutAdService = scoutAdService;
    }

    @PostMapping(value = "/uploadScoutAds/{username}", consumes = {"multipart/form-data"})
    public ScoutAd uploadAds(@PathVariable("username") String username, @RequestParam("content") String content, @RequestParam("file") MultipartFile file) throws Exception{
        return this.scoutAdService.uploadAds(username, content, file);
    }

    @PutMapping("/updateScoutAd/{adId}")
    public ScoutAd updateAd(@PathVariable("adId") Integer adId, @RequestParam("content") String content, @RequestParam("file") MultipartFile file) {
        return scoutAdService.updateAd(adId, content, file);
    }

    @DeleteMapping("/deleteScoutAd/{adId}")
    public void deleteAd(@PathVariable("adId") Integer adId) {
        scoutAdService.deleteAdById(adId);
    }

    @GetMapping("/scoutAds")
    public List<ScoutAd> getAllPlayerAds() {
        return scoutAdService.getAllScoutAds();
    }

    @GetMapping("/adsScoutImage/{scoutAdId}")
    public ResponseEntity<byte[]> getScoutAdImageById(@PathVariable Integer scoutAdId) {
        byte[] imageBytes = scoutAdService.getScoutAdImageById(scoutAdId);

        if (imageBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // vagy MediaType.IMAGE_PNG stb., a kép típusától függően

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
