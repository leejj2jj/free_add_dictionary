package com.freeadddict.dict.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.freeadddict.dict.user.domain.User;
import com.freeadddict.dict.user.dto.AddUserRequest;
import com.freeadddict.dict.user.repository.UserRepository;

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
}
