package com.freeadddictionary.dict.shared.exception;

import org.springframework.http.HttpStatus;

public class ExternalErrorException extends BusinessException {

  public ExternalErrorException() {}

  public ExternalErrorException(String message) {
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
