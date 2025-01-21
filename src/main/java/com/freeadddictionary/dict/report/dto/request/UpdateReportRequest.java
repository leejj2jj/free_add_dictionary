package com.freeadddictionary.dict.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateReportRequest {

  @NotBlank
  @Size(max = 100)
  private String title;

  @NotBlank
  private String content;
}
