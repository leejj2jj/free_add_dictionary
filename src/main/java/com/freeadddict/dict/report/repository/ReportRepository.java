package com.freeadddict.dict.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freeadddict.dict.report.domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
