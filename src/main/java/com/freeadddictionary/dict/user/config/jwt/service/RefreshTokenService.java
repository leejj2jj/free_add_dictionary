package com.freeadddictionary.dict.user.config.jwt.service;

import org.springframework.stereotype.Service;

import com.freeadddictionary.dict.user.config.jwt.domain.RefreshToken;
import com.freeadddictionary.dict.user.config.jwt.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshToken findByRefreshToken(String refreshToken) {
    return refreshTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
  }

}