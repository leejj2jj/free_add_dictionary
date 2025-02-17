package com.freeadddictionary.dict.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportReplyAddRequest {

  @NotBlank(message = "본문은 필수입니다.")
  private String content;
}
