package com.freeadddictionary.dict.member.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAccessTokenResponse {

  private String accessToken;
}
