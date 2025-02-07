package com.freeadddictionary.dict.report.dto.request;

import com.freeadddictionary.dict.report.domain.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddReportRequest {

  @NotBlank
  @Size(max = 100)
  private String title;

  @NotBlank private String content;

  public Report toEntity() {
    return Report.builder().title(title).content(content).build();
  }
}
