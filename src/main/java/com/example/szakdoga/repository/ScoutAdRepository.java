package com.example.szakdoga.repository;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.Scout;
import com.example.szakdoga.model.ScoutAd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoutAdRepository extends JpaRepository<ScoutAd, Integer> {
    List<ScoutAd> findByScout(Scout scout);
}
