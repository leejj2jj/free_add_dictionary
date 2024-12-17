package com.freeadddictionary.dict.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.dto.AddUserRequest;
import com.freeadddictionary.dict.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Long save(AddUserRequest dto) {
    return userRepository.save(User.builder()
        .email(dto.getEmail())
        .password(bCryptPasswordEncoder.encode(dto.getPassword()))
        .nickname(dto.getNickname())
        .name(dto.getName())
        .phone(dto.getPhone())
        .receivingEmail(dto.isReceivingEmail())
        .registerDate(dto.getRegisterDate())
        .build()).getId();
  }

  public User findById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
  }
}
