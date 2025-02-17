package com.freeadddictionary.dict.reportReply.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportReplyUpdateRequest {

  @NotBlank(message = "본문은 필수입니다.")
  private String content;
}
