package com.freeadddictionary.dict.util;

import org.springframework.util.StringUtils;

public class ValidationUtil {

  public static void validateEmail(String email) {
    if (!StringUtils.hasText(email)) {
      throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
    }
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new IllegalArgumentException("유효한 이메일 주소를 입력하세요.");
    }
  }

  public static void validatePassword(String password) {
    if (!StringUtils.hasText(password)) {
      throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
    }
    if (password.length() < 10) {
      throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
    }
  }

  public static void validateNickname(String nickname) {
    if (!StringUtils.hasText(nickname)) {
      throw new IllegalArgumentException("닉네임은 필수 입력값입니다.");
    }
  }

  private ValidationUtil() {
    throw new IllegalStateException("Utility class");
  }
}
