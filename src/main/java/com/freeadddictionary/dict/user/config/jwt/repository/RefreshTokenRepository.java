package com.freeadddictionary.dict.user.config.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freeadddictionary.dict.user.config.jwt.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByUserId(Long userId);

  Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
