package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.domain.PasswordResetToken;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.repository.PasswordResetTokenRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

  private final UserRepository userRepository;
  private final PasswordResetTokenRepository tokenRepository;
  private final JavaMailSender mailSender;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.url:http://localhost:8888}")
  private String appUrl;

  @Transactional
  public boolean sendPasswordResetEmail(String email) {
    User user = userRepository.findByEmail(email).orElse(null);
    if (user == null) {
      return false;
    }

    String token = UUID.randomUUID().toString();
    PasswordResetToken resetToken = new PasswordResetToken();
    resetToken.setUser(user);
    resetToken.setToken(token);
    resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
    tokenRepository.save(resetToken);

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(user.getEmail());
    mailMessage.setSubject("비밀번호 재설정");
    mailMessage.setText(
        "비밀번호를 재설정하려면 다음 링크를 클릭하세요: " + appUrl + "/user/password-reset-confirm?token=" + token);

    mailSender.send(mailMessage);
    return true;
  }

  @Transactional(readOnly = true)
  public boolean validatePasswordResetToken(String token) {
    PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
    return resetToken != null
        && !resetToken.isExpired()
        && resetToken.getExpiryDate().isAfter(LocalDateTime.now());
  }

  @Transactional
  public boolean resetPassword(String token, String newPassword) {
    PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
    if (resetToken == null
        || resetToken.isExpired()
        || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
      return false;
    }

    if (!isPasswordStrong(newPassword)) {
      return false;
    }

    User user = resetToken.getUser();
    user.update(user.getNickname(), passwordEncoder.encode(newPassword));
    userRepository.save(user);

    tokenRepository.delete(resetToken);
    return true;
  }

  private boolean isPasswordStrong(String password) {
    int strength = 0;

    if (password.length() >= 8) strength++;
    if (password.matches(".*[A-Z].*")) strength++;
    if (password.matches(".*[a-z].*")) strength++;
    if (password.matches(".*[0-9].*")) strength++;
    if (password.matches(".*[!@#$%^&*].*")) strength++;

    return strength >= 3;
  }
}
