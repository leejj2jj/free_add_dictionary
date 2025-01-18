package com.freeadddictionary.dict.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freeadddictionary.dict.domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
