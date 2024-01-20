package com.example.szakdoga.repository;

import com.example.szakdoga.model.Report;
import com.example.szakdoga.model.SendMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
}
