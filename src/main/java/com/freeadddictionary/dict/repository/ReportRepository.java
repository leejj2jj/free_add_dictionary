package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Report;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {

  Page<Report> findByAuthorEmail(String email, Pageable pageable);

  Page<Report> findByResolved(boolean resolved, Pageable pageable);

  @Query("SELECT COUNT(r) FROM Report r WHERE r.resolved = false")
  long countUnresolvedReports();

  @Query("SELECT COUNT(r) FROM Report r WHERE r.createdAt >= :startTime")
  long countTodayNewReports(LocalDateTime startTime);
}
