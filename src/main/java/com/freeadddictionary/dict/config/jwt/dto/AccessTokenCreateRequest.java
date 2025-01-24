package com.freeadddictionary.dict.config.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenCreateRequest {

  private String refreshToken;
}
