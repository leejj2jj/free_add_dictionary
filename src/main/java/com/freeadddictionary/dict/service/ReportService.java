package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.domain.Report;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.ReportRequest;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.ReportQueryRepository;
import com.freeadddictionary.dict.repository.ReportRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

  private final ReportRepository reportRepository;
  private final ReportQueryRepository reportQueryRepository;
  private final UserRepository userRepository;

  public Page<Report> getReports(Boolean resolved, Pageable pageable) {
    return reportQueryRepository.findByResolved(resolved, pageable);
  }

  public Page<Report> getUserReports(String email, Pageable pageable) {
    return reportRepository.findByAuthorEmail(email, pageable);
  }

  @Transactional
  public Report createReport(ReportRequest request, String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

    Report report =
        Report.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .authorEmail(email)
            .user(user)
            .build();

    return reportRepository.save(report);
  }

  @Transactional
  public Report resolveReport(Long id) {
    Report report =
        reportRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
    report.resolve();
    return report;
  }

  @Transactional
  public void deleteReport(Long id) {
    if (!reportRepository.existsById(id)) {
      throw new ResourceNotFoundException("Report", "id", id);
    }
    reportRepository.deleteById(id);
  }

  public Report getReport(Long id) {
    return reportRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
  }
}
