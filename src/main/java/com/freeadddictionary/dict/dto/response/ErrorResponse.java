package com.freeadddictionary.dict.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

  private final String message;
  private final LocalDateTime timestamp = LocalDateTime.now();
}
