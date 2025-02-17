package com.freeadddictionary.dict.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportAddRequest {

  @NotBlank(message = "제목은 필수입니다.")
  @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
  private String title;

  @NotBlank(message = "본문은 필수입니다.")
  private String content;
}
