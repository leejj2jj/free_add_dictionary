package com.freeadddictionary.dict.user.service;

import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.dto.request.UserAddRequest;
import com.freeadddictionary.dict.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Long save(UserAddRequest dto) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    return userRepository
        .save(
            User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phone(dto.getPhone())
                .receivingEmail(dto.isReceivingEmail())
                .build())
        .getId();
  }

  public User findById(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
  }

  public User findByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
  }
}