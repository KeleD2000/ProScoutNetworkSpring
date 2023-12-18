package com.example.szakdoga.repository;

import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.Scout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoutRepository extends JpaRepository<Scout, Integer> {
    Optional<Scout> findByUserId(Integer userId);
}

