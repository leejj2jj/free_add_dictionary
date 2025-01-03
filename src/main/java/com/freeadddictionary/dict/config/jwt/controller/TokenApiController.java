package com.freeadddictionary.dict.config.jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.freeadddictionary.dict.config.jwt.dto.CreateAccessTokenRequest;
import com.freeadddictionary.dict.config.jwt.dto.CreateAccessTokenResponse;
import com.freeadddictionary.dict.config.jwt.service.TokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

  private final TokenService tokenService;

  @PostMapping("/api/token")
  public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {

    String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

    return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
  }

}
