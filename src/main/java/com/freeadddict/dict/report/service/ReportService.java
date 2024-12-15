package com.freeadddict.dict.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freeadddict.dict.report.domain.Report;
import com.freeadddict.dict.report.dto.AddReportRequest;
import com.freeadddict.dict.report.dto.UpdateReportRequest;
import com.freeadddict.dict.report.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {

  private final ReportRepository reportRepository;

  public Report save(AddReportRequest report) {
    return reportRepository.save(report.toEntity());
  }

  public List<Report> findAll() {
    return reportRepository.findAll();
  }

  public Report findById(long id) {
    return reportRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Report not found: " + id));
  }

  public void delete(long id) {
    reportRepository.deleteById(id);
  }

  @Transactional
  public Report update(long id, UpdateReportRequest request) {
    Report report = reportRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Report not found: " + id));

    report.update(request.getTitle(), request.getContent());

    return report;
  }

}
