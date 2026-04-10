package com.agrovalue.backend.repository;

import com.agrovalue.backend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select count(r) from Report r where r.status <> 'RESOLVED'")
    long countOpenIssues();
}
