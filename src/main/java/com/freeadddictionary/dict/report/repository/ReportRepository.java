package com.freeadddictionary.dict.report.repository;

import com.freeadddictionary.dict.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {}
