package com.freeadddictionary.dict.user.service;

import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.dto.request.UserAddRequest;
import com.freeadddictionary.dict.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public Long save(UserAddRequest dto) {
    return userRepository
        .save(
            User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
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
