package com.freeadddictionary.dict.report.service;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.dto.request.ReportAddRequest;
import com.freeadddictionary.dict.report.dto.request.ReportUpdateRequest;
import com.freeadddictionary.dict.report.repository.ReportRepository;
import com.freeadddictionary.dict.user.domain.DictUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;

  public Report save(ReportAddRequest request, DictUser user) {
    return reportRepository.save(
        Report.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .user(user)
            .build());
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
  public Report update(long id, ReportUpdateRequest request) {
    Report report =
        reportRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + id));

    report.update(request.getTitle(), request.getContent());

    return report;
  }
}
