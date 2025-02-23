package com.freeadddictionary.dict.dto.request;

import com.freeadddictionary.dict.domain.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequest {
  private Long id;

  @NotBlank(message = "제목을 입력해주세요")
  @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
  private String title;

  @NotBlank(message = "내용을 입력해주세요")
  @Size(max = 2000, message = "내용은 2000자를 초과할 수 없습니다")
  private String content;

  public ReportRequest(Report report) {
    this.id = report.getId();
    this.title = report.getTitle();
    this.content = report.getContent();
  }
}
