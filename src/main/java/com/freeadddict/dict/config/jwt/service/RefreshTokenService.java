package com.freeadddict.dict.config.jwt.service;

import org.springframework.stereotype.Service;

import com.freeadddict.dict.config.jwt.domain.RefreshToken;
import com.freeadddict.dict.config.jwt.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshToken findByRefreshToken(String refreshToken) {
    return refreshTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
  }

}