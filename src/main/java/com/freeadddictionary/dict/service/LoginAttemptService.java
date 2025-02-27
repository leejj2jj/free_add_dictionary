package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.repository.UserRepository;
import com.freeadddictionary.dict.util.LoggingUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

  private final UserRepository userRepository;

  private final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, LocalDateTime> blockTimeCache = new ConcurrentHashMap<>();

  @Value("${security.login.max-attempts:5}")
  private int MAX_ATTEMPTS;

  @Value("${security.login.block-duration-minutes:30}")
  private int BLOCK_DURATION_MINUTES;

  public void loginSucceeded(String key) {
    attemptsCache.remove(key);
    blockTimeCache.remove(key);
  }

  public void loginFailed(String key, String email) {
    if (isTemporarilyBlocked(key)) {
      return;
    }

    int attempts = attemptsCache.getOrDefault(key, 0);
    attempts++;
    attemptsCache.put(key, attempts);

    LoggingUtil.logStructured(
        "login.failed",
        Map.of(
            "ip", key,
            "email", email,
            "attempts", attempts,
            "maxAttempts", MAX_ATTEMPTS));

    if (attempts >= MAX_ATTEMPTS) {
      blockTimeCache.put(key, LocalDateTime.now());

      // 사용자 계정 잠금 처리 (이메일 주소가 있는 경우만)
      if (email != null && !email.isEmpty()) {
        lockUserAccount(email);
      }

      LoggingUtil.logStructured(
          "account.blocked",
          Map.of(
              "ip", key,
              "email", email,
              "blockDuration", BLOCK_DURATION_MINUTES));
    }
  }

  @Transactional
  protected void lockUserAccount(String email) {
    userRepository
        .findByEmail(email)
        .ifPresent(
            user -> {
              user.lock();
              userRepository.save(user);

              LoggingUtil.logAudit(
                  "LOCK",
                  "User",
                  email,
                  "system",
                  Map.of("reason", "Maximum login attempts exceeded"));
            });
  }

  public boolean isBlocked(String key, String email) {
    // IP 기반 임시 차단 확인
    if (isTemporarilyBlocked(key)) {
      return true;
    }

    // 사용자 계정 잠금 확인
    if (email != null && !email.isEmpty()) {
      return userRepository.findByEmail(email).map(User::isLocked).orElse(false);
    }

    return false;
  }

  private boolean isTemporarilyBlocked(String key) {
    LocalDateTime blockTime = blockTimeCache.get(key);
    if (blockTime == null) {
      return false;
    }

    LocalDateTime unblockTime = blockTime.plusMinutes(BLOCK_DURATION_MINUTES);
    boolean stillBlocked = LocalDateTime.now().isBefore(unblockTime);

    if (!stillBlocked) {
      // 차단 시간이 지났으면 캐시에서 제거
      blockTimeCache.remove(key);
      attemptsCache.remove(key);
    }

    return stillBlocked;
  }

  public String getClientIP(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }

  public int getRemainingAttempts(String key) {
    int attempts = attemptsCache.getOrDefault(key, 0);
    return Math.max(0, MAX_ATTEMPTS - attempts);
  }

  public long getBlockTimeRemaining(String key) {
    LocalDateTime blockTime = blockTimeCache.get(key);
    if (blockTime == null) {
      return 0;
    }

    LocalDateTime unblockTime = blockTime.plusMinutes(BLOCK_DURATION_MINUTES);
    long diff = java.time.Duration.between(LocalDateTime.now(), unblockTime).getSeconds();
    return diff > 0 ? diff : 0;
  }
}
