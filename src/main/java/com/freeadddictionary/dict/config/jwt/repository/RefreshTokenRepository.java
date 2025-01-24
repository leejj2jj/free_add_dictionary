package com.freeadddictionary.dict.config.jwt.repository;

import com.freeadddictionary.dict.config.jwt.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByUserId(Long userId);

  Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
