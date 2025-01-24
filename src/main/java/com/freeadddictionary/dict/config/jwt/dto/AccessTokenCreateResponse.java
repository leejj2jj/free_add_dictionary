package com.freeadddictionary.dict.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenCreateResponse {

  private String accessToken;
}
