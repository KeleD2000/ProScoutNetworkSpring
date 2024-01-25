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
            "FROM player p LEFT JOIN player_ad pa ON p.id = pa.player_id " +
            "GROUP BY p.id, p.first_name, p.last_name" +
            ") AS player_ads " +
            "UNION " +
            "SELECT 'Scout' AS role, AVG(ad_count) AS avg_ad_count FROM (" +
            "SELECT s.id AS user_id, s.first_name, s.last_name, COUNT(id) AS ad_count " +
            "FROM scout s LEFT JOIN scout_ad sa ON s.id = sa.scout_id " +
            "GROUP BY s.id, s.first_name, s.last_name" +
            ") AS scout_ads", nativeQuery = true)
    List<Object[]> calculateAverageAdCount();

    @Query(value = "SELECT p.sport, (COUNT(DISTINCT pa.playerad_id) / (SELECT COUNT(pa2.playerad_id) " +
            "FROM player_ad pa2)) * 100 AS atlag_szazalek FROM player p LEFT JOIN player_ad pa " +
            "ON p.id = pa.player_id GROUP BY p.sport", nativeQuery = true)
    List<Object[]> calculateAveragePercentageBySport();

    @Query(value = "WITH RankedPlayers AS (" +
            "SELECT " +
            "    player.sport, " +
            "    CONCAT(player.last_name, ' ', player.first_name) AS player_name, " +
            "    COUNT(player_ad.playerad_id) AS ad_count, " +
            "    ROW_NUMBER() OVER (PARTITION BY player.sport ORDER BY COUNT(player_ad.playerad_id) DESC) AS row_num " +
            "FROM player " +
            "LEFT JOIN player_ad ON player.id = player_ad.player_id " +
            "GROUP BY player.sport, player_name" +
            ") " +
            "SELECT sport, player_name, ad_count " +
            "FROM RankedPlayers " +
            "WHERE row_num = 1", nativeQuery = true)
    List<Object[]> getTopPlayerBySport();


}


