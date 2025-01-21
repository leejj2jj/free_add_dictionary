package com.freeadddictionary.dict.member.config.jwt.service;

import org.springframework.stereotype.Service;

import com.freeadddictionary.dict.member.config.jwt.domain.RefreshToken;
import com.freeadddictionary.dict.member.config.jwt.repository.RefreshTokenRepository;

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