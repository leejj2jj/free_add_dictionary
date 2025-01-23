package com.freeadddictionary.dict.shared.exception;

import org.springframework.http.HttpStatus;

public class InternalErrorException extends BusinessException {

  public InternalErrorException() {}

  public InternalErrorException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  @Override
  public boolean isNecessaryToLog() {
    return true;
  }
}
