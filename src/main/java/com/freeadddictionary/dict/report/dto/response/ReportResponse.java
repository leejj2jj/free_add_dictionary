package com.freeadddictionary.dict.report.dto.response;

import com.freeadddictionary.dict.member.domain.Member;
import com.freeadddictionary.dict.report.domain.Report;
import lombok.Getter;

@Getter
public class ReportResponse {

  private String title;
  private String content;
  private Member member;

  public ReportResponse(Report report) {
    this.title = report.getTitle();
    this.content = report.getContent();
    this.member = report.getMember();
  }
}
