package com.freeadddictionary.dict.word.exception;

import com.freeadddictionary.dict.shared.exception.ResourceNotFoundException;

public class WordNotFoundException extends ResourceNotFoundException {

  public WordNotFoundException() {}

  public WordNotFoundException(String message) {
    super(message);
  }
}
