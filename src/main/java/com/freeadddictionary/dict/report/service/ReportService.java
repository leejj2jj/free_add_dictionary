package com.freeadddictionary.dict.report.service;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.dto.request.ReportAddRequest;
import com.freeadddictionary.dict.report.dto.request.ReportUpdateRequest;
import com.freeadddictionary.dict.report.repository.ReportRepository;
import com.freeadddictionary.dict.shared.exception.DataNotFoundException;
import com.freeadddictionary.dict.user.domain.DictUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;

  public Page<Report> getList(int page, String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createdAt"));
    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
    return reportRepository.findAllByKeyword(kw, pageable);
  }

  public Report getReport(Long id) {
    Optional<Report> report = reportRepository.findById(id);
    if (report.isPresent()) {
      return report.get();
    } else {
      throw new DataNotFoundException("Report not found with id: " + id);
    }
  }

  public void create(ReportAddRequest request, DictUser user) {
    Report report =
        Report.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .author(user)
            .build();
    reportRepository.save(report);
  }

  public void modify(Report report, ReportUpdateRequest request) {
    report.update(request.getTitle(), request.getContent());
    reportRepository.save(report);
  }

  public void delete(Report report) {
    reportRepository.delete(report);
  }
}
