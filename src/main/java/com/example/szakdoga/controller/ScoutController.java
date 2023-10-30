package com.example.szakdoga.controller;
import com.example.szakdoga.model.Scout;
import com.example.szakdoga.services.ScoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 0)
@RequestMapping("/api")
public class ScoutController {

    @Autowired
    private ScoutService scoutService;

    @PostMapping("/register_scout")
    public ResponseEntity<String> registerScout(@RequestBody Scout scoutData) {
        System.out.println("ANYÁD");
        System.out.println(scoutData);
        scoutService.registerScout(scoutData);
        return ResponseEntity.ok("Sikeres regisztráció!");
    }
}
