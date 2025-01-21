package com.freeadddictionary.dict.user.config.jwt.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenProvider tokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;

  public String createNewAccessToken(String refreshToken) {

    if (!tokenProvider.validToken(refreshToken)) {
      throw new IllegalArgumentException("Unexpected token");
    }

    Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
    User user = userService.findById(userId);

    return tokenProvider.generateToken(user, Duration.ofHours(2));
  }

}
