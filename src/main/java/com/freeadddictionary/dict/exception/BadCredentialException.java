package com.freeadddictionary.dict.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadCredentialException extends RuntimeException {

  public BadCredentialException(String message) {
    super(message);
  }

  public BadCredentialException(String message, Throwable cause) {
    super(message, cause);
  }
}
