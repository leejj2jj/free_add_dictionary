package com.freeadddictionary.dict.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerRuntimeException extends RuntimeException {

  public ServerRuntimeException(String message) {
    super(message);
  }

  public ServerRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
