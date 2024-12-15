package com.freeadddict.dict.report.dto;

import com.freeadddict.dict.report.domain.Report;
import com.freeadddict.dict.user.domain.User;

import lombok.Getter;

@Getter
public class ReportResponse {

  private String title;
  private String content;
  private User user;

  public ReportResponse(Report report) {
    this.title = report.getTitle();
    this.content = report.getContent();
    this.user = report.getUser();
  }

}
