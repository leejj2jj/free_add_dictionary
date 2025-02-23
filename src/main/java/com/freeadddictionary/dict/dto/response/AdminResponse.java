package com.freeadddictionary.dict.dto.response;

import com.freeadddictionary.dict.domain.Admin;
import java.time.LocalDateTime;

public record AdminResponse(Long id, String email, String nickname, LocalDateTime createdAt) {

  public static AdminResponse from(Admin admin) {
    return new AdminResponse(
        admin.getId(), admin.getEmail(), admin.getNickname(), admin.getCreatedAt());
  }
}
