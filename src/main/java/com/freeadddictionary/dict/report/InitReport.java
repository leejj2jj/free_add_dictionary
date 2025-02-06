package com.freeadddictionary.dict.report;

import com.freeadddictionary.dict.report.domain.Report;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitReport {

  private final InitReportService initReportService;

  @PostConstruct
  public void init() {
    initReportService.init();
  }

  @Component
  static class InitReportService {

    private final EntityManager em;

    public InitReportService(EntityManager em) {
      this.em = em;
    }

    @Transactional
    public void init() {
      for (int i = 1; i < 51; i++) {
        em.persist(Report.builder().title("테스트 제목입니다(" + i + ").").content("테스트 내용입니다.").build());
      }
    }
  }
}
