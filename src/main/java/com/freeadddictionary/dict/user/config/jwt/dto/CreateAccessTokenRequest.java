package com.freeadddictionary.dict.user.config.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequest {

  private String refreshToken;

}