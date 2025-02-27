package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.PasswordResetToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  Optional<PasswordResetToken> findByToken(String token);

  void deleteByUser_Id(Long userId);
}
