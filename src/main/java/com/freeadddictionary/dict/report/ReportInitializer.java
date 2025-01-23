package com.freeadddictionary.dict.report;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.repository.ReportRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportInitializer {

  private final ReportRepository reportRepository;

  @PostConstruct
  public void init() {
    for (int i = 1; i < 51; i++) {
      Report report = Report.builder().title("테스트 제목입니다(" + i + ").").content("테스트 내용입니다.").build();
      reportRepository.save(report);
    }
  }
}
