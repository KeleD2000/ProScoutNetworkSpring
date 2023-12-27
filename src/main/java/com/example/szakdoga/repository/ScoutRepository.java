package com.example.szakdoga.repository;

import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.Scout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoutRepository extends JpaRepository<Scout, Integer> {
    Optional<Scout> findByUserId(Integer userId);

    @Query("SELECT s FROM Scout s WHERE " +
            "s.first_name LIKE %:searchTerm% OR " +
            "s.last_name LIKE %:searchTerm% OR " +
            "s.team LIKE %:searchTerm% OR " +
            "s.sport LIKE %:searchTerm%")
    List<Scout> findByMultipleFields(@Param("searchTerm") String searchTerm);
}

