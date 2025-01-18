package com.freeadddictionary.dict.dto;

import com.freeadddictionary.dict.domain.Report;

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
