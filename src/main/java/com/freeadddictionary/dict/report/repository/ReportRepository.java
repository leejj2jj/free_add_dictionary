package com.freeadddictionary.dict.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freeadddictionary.dict.report.domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
