package com.freeadddictionary.dict.shared.exception;

import org.springframework.http.HttpStatus;

public class NotAuthorizedException extends BusinessException {

  public NotAuthorizedException() {}

  public NotAuthorizedException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.FORBIDDEN;
  }

  @Override
  public boolean isNecessaryToLog() {
    return false;
  }
}
