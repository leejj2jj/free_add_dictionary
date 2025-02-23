package com.freeadddictionary.dict.dto.request;

import com.freeadddictionary.dict.domain.Admin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminRequest(
    Long id,
    @NotBlank(message = "이메일을 입력하세요.") @Email(message = "유효한 이메일 주소를 입력하세요.") String email,
    @NotBlank(message = "비밀번호를 입력하세요.") @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
        String password,
    @NotBlank(message = "닉네임을 입력하세요.") String nickname) {
  public AdminRequest {
    if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new IllegalArgumentException("유효한 이메일 주소를 입력하세요.");
    }
  }

  public Admin toEntity(String encodedPassword) {
    return Admin.builder().id(id).email(email).password(encodedPassword).nickname(nickname).build();
  }
}
