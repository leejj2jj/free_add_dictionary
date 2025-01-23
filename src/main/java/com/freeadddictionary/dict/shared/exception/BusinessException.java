package com.freeadddictionary.dict.shared.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

  public BusinessException() {}

  public BusinessException(String message) {
    super(message);
  }

  public abstract HttpStatus getHttpStatus();

  public abstract boolean isNecessaryToLog();
}
