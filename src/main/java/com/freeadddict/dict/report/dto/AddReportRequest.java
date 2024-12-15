package com.freeadddict.dict.report.dto;

import com.freeadddict.dict.report.domain.Report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddReportRequest {

  private String title;
  private String content;

  public Report toEntity() {
    return Report.builder()
        .title(title)
        .content(content)
        .build();
  }

}
