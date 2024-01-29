package com.example.szakdoga.repository;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.PlayerAd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerAdRepository extends JpaRepository<PlayerAd, Integer> {

    List<PlayerAd> findByPlayer(Player player);
}
