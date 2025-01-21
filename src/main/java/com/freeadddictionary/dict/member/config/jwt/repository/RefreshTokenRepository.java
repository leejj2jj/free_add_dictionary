package com.freeadddictionary.dict.member.config.jwt.repository;

import com.freeadddictionary.dict.member.config.jwt.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByMemberId(Long memberId);

  Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
