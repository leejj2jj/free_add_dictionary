package com.freeadddictionary.dict.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReportRequest {

  @NotBlank
  @Size(max = 100)
  private String title;

  @NotBlank
  private String content;
}
