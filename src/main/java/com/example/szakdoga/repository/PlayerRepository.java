package com.example.szakdoga.repository;

import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.PlayerInfo;
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

    @Query("SELECT new com.example.szakdoga.model.PlayerInfo(p, p.user.username) FROM Player p")
    List<PlayerInfo> findAllPlayersWithUsernames();

    @Query(value = "SELECT 'Player' AS role, AVG(ad_count) AS avg_ad_count FROM (" +
            "SELECT p.id AS user_id, p.first_name, p.last_name, COUNT(id) AS ad_count " +
            "FROM player p LEFT JOIN player_ad pa ON p.id = id " +
            "GROUP BY p.id, p.first_name, p.last_name" +
            ") AS player_ads", nativeQuery = true)
    Object calculateAverageAdCountPlayer();


    @Query(value = "SELECT 'Scout' AS role, AVG(COUNT(id)) AS avg_ad_count FROM scout s LEFT JOIN scout_ad sa ON s.id = id GROUP BY s.id, s.first_name, s.last_name", nativeQuery = true)
    Object calculateAverageAdCountScout();



}


