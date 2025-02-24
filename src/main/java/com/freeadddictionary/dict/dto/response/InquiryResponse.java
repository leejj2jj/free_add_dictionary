package com.freeadddictionary.dict.dto.response;

import com.freeadddictionary.dict.domain.Inquiry;
import java.time.LocalDateTime;

public record InquiryResponse(
    Long id,
    String title,
    String content,
    boolean isResolved,
    String authorEmail,
    LocalDateTime createdAt) {

  public static InquiryResponse from(Inquiry inquiry) {
    return new InquiryResponse(
        inquiry.getId(),
        inquiry.getTitle(),
        inquiry.getContent(),
        inquiry.isResolved(),
        inquiry.getAuthorEmail(),
        inquiry.getCreatedAt());
  }
}
