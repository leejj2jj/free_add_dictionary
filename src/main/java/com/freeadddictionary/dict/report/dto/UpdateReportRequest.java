package com.freeadddictionary.dict.report.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReportRequest {

  @NotBlank
  @Length(max = 100)
  private String title;

  @NotBlank
  private String content;
}
