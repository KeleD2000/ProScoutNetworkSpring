package com.example.szakdoga.repository;

import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    Optional<Player> findByUserId(Integer userId);

    @Query("SELECT p FROM Player p WHERE " +
            "p.first_name LIKE %:searchTerm% OR " +
            "p.last_name LIKE %:searchTerm% OR " +
            "p.position LIKE %:searchTerm% OR " +
            "p.sport LIKE %:searchTerm% OR " +
            "CAST(p.age AS string) LIKE %:searchTerm%")
    List<Player> findByMultipleFields(@Param("searchTerm") String searchTerm);
}
