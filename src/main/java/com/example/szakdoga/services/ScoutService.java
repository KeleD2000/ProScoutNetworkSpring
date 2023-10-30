package com.example.szakdoga.services;

import com.example.szakdoga.model.Scout;
import com.example.szakdoga.repository.ScoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoutService {
    @Autowired
    private ScoutRepository scoutRepository;

    public void registerScout(Scout scout) {
        scoutRepository.save(scout);

    }
}
