package com.example.szakdoga.repository;

import com.example.szakdoga.model.Scout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoutRepository extends JpaRepository<Scout, Integer> {
    // Egyéb egyedi lekérdezések vagy műveletek
}

