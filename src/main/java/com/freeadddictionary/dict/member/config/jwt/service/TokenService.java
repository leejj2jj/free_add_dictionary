package com.freeadddictionary.dict.member.config.jwt.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.freeadddictionary.dict.member.domain.Member;
import com.freeadddictionary.dict.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenProvider tokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final MemberService memberService;

  public String createNewAccessToken(String refreshToken) {

    if (!tokenProvider.validToken(refreshToken)) {
      throw new IllegalArgumentException("Unexpected token");
    }

    Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
    Member member = memberService.findById(memberId);

    return tokenProvider.generateToken(member, Duration.ofHours(2));
  }

}
