package com.freeadddictionary.dict.report.dto.response;

import com.freeadddictionary.dict.report.domain.Report;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReportViewResponse {

  private Long id;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;

  public ReportViewResponse(Report report) {
    this.id = report.getId();
    this.title = report.getTitle();
    this.content = report.getContent();
    this.createdAt = report.getCreatedAt();
    this.lastModifiedAt = report.getLastModifiedAt();
  }
}
