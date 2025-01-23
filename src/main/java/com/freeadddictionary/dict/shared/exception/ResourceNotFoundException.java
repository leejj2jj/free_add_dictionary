package com.freeadddictionary.dict.shared.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {

  public ResourceNotFoundException() {}

  public ResourceNotFoundException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

  @Override
  public boolean isNecessaryToLog() {
    return false;
  }
}
