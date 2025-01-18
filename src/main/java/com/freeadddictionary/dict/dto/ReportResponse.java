package com.freeadddictionary.dict.dto;

import com.freeadddictionary.dict.domain.Report;
import com.freeadddictionary.dict.domain.User;

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