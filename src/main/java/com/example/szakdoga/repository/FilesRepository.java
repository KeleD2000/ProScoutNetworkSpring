package com.example.szakdoga.repository;

import com.example.szakdoga.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<File, Long> {
}
