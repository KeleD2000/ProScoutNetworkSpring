package com.example.szakdoga.repository;

import com.example.szakdoga.model.File;
import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilesRepository extends JpaRepository<File, Long> {
    List<File> findByUser(User user);

    @Query("SELECT f.file_path FROM File f WHERE f.user.id = :userId AND f.type = 'profilpic'")
    String findProfilePicturePathByUserId(@Param("userId") Integer userId);


    @Modifying
    @Transactional
    void deleteByUser(User user);
}
