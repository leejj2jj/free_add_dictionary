package com.freeadddictionary.dict.shared.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException {

  public BadRequestException() {}

  public BadRequestException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

  @Override
  public boolean isNecessaryToLog() {
    return false;
  }
}
