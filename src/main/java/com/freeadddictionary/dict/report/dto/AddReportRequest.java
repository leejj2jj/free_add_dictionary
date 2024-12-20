package com.freeadddictionary.dict.report.dto;

import org.hibernate.validator.constraints.Length;

import com.freeadddictionary.dict.report.domain.Report;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddReportRequest {

  @NotBlank
  @Length(max = 100)
  private String title;

  @NotBlank
  private String content;

  public Report toEntity() {
    return Report.builder()
        .title(title)
        .content(content)
        .build();
  }

}
