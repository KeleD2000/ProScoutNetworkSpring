package com.example.szakdoga.repository;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesRepository extends JpaRepository<File, Long> {
    List<File> findByPlayer(Player player);
}
