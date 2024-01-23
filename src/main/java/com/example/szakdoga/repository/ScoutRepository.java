package com.example.szakdoga.repository;

import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.PlayerInfo;
import com.example.szakdoga.model.Scout;
import com.example.szakdoga.model.ScoutInfo;
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

    @Query("SELECT new com.example.szakdoga.model.ScoutInfo(s, s.user.username) FROM Scout s")
    List<ScoutInfo> findAllScoutsWithUsernames();

    @Query(value = "SELECT p.sport, (COUNT(DISTINCT pa.scoutad_id) / (SELECT COUNT(pa2.scoutad_id) FROM scout_ad pa2)) * 100 AS atlag_szazalek FROM scout p LEFT JOIN scout_ad pa ON p.id = pa.scout_id GROUP BY p.sport", nativeQuery = true)
    List<Object[]> calculateAveragePercentageBySport();

    @Query(value = "WITH RankedScout AS (" +
            "SELECT " +
            "    scout.sport, " +
            "    CONCAT(scout.last_name, ' ', scout.first_name) AS scout_name, " +
            "    COUNT(scout_ad.scoutad_id) AS ad_count, " +
            "    ROW_NUMBER() OVER (PARTITION BY scout.sport ORDER BY COUNT(scout_ad.scoutad_id) DESC) AS row_num " +
            "FROM scout " +
            "LEFT JOIN scout_ad ON scout.id = scout_ad.scout_id " +
            "GROUP BY scout.sport, scout_name" +
            ") " +
            "SELECT sport, scout_name, ad_count " +
            "FROM RankedScout " +
            "WHERE row_num = 1", nativeQuery = true)
    List<Object[]> getTopScoutBySport();
}


