package com.freeadddictionary.dict.report.service;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.dto.request.AddReportRequest;
import com.freeadddictionary.dict.report.dto.request.UpdateReportRequest;
import com.freeadddictionary.dict.report.repository.ReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;

  public Report save(AddReportRequest report) {
    return reportRepository.save(report.toEntity());
  }

  public List<Report> findAll() {
    return reportRepository.findAll();
  }

  public Report findById(long id) {
    return reportRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Report not found: " + id));
  }

  public void delete(long id) {
    reportRepository.deleteById(id);
  }

  @Transactional
  public Report update(long id, UpdateReportRequest request) {
    Report report =
        reportRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + id));

    report.update(request.getTitle(), request.getContent());

    return report;
  }
}
