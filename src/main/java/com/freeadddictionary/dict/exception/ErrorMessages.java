package com.freeadddictionary.dict.exception;

public final class ErrorMessages {

  // 리소스 관련
  public static final String RESOURCE_NOT_FOUND = "%s을(를) 찾을 수 없습니다: %s = %s";
  public static final String DUPLICATE_RESOURCE = "이미 존재하는 %s입니다: %s = %s";

  // 인증/인가 관련
  public static final String ACCESS_DENIED = "이 작업을 수행할 권한이 없습니다";
  public static final String INVALID_CREDENTIALS = "잘못된 인증 정보입니다";
  public static final String ACCOUNT_LOCKED = "계정이 잠겼습니다. 관리자에게 문의하세요";

  // 검증 관련
  public static final String VALIDATION_FAILED = "입력값 검증에 실패했습니다";
  public static final String INVALID_FORMAT = "잘못된 형식입니다";

  // 서버 오류
  public static final String INTERNAL_SERVER_ERROR = "서버에서 오류가 발생했습니다. 나중에 다시 시도해 주세요";

  private ErrorMessages() {}
}
