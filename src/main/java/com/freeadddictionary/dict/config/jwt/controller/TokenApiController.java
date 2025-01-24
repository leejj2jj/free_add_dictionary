package com.freeadddictionary.dict.config.jwt.controller;

import com.freeadddictionary.dict.config.jwt.dto.AccessTokenCreateRequest;
import com.freeadddictionary.dict.config.jwt.dto.AccessTokenCreateResponse;
import com.freeadddictionary.dict.config.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

  private final TokenService tokenService;

  @PostMapping("/api/token")
  public ResponseEntity<AccessTokenCreateResponse> createNewAccessToken(
      @RequestBody AccessTokenCreateRequest request) {

    String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new AccessTokenCreateResponse(newAccessToken));
  }
}
