package com.freeadddictionary.dict.dto.response;

import com.freeadddictionary.dict.domain.Report;
import java.time.LocalDateTime;

public record ReportResponse(
    Long id,
    String title,
    String content,
    boolean isResolved,
    String authorEmail,
    LocalDateTime createdAt) {

  public static ReportResponse from(Report report) {
    return new ReportResponse(
        report.getId(),
        report.getTitle(),
        report.getContent(),
        report.isResolved(),
        report.getAuthorEmail(),
        report.getCreatedAt());
  }
}
