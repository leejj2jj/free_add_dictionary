package com.freeadddictionary.dict.config;

import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) {
    if (!userRepository.existsByEmail("admin@freeadddictionary.com")) {
      User admin =
          User.builder()
              .email("admin@freeadddictionary.com")
              .password(passwordEncoder.encode("admin123"))
              .nickname("관리자")
              .role(Role.ADMIN)
              .build();

      userRepository.save(admin);
    }
  }
}
