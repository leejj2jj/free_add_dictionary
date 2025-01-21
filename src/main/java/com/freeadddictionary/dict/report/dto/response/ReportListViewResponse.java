package com.freeadddictionary.dict.report.dto.response;

import com.freeadddictionary.dict.report.domain.Report;
import lombok.Getter;

@Getter
public class ReportListViewResponse {

  private final Long id;
  private final String title;
  private final String content;

  public ReportListViewResponse(Report report) {
    this.id = report.getId();
    this.title = report.getTitle();
    this.content = report.getContent();
  }
}
