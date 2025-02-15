package com.freeadddictionary.dict.user.service;

import com.freeadddictionary.dict.user.domain.DictUser;
import com.freeadddictionary.dict.user.dto.request.UserRegisterRequest;
import com.freeadddictionary.dict.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Long save(UserRegisterRequest dto) {
    return userRepository
        .save(
            DictUser.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phone(dto.getPhone())
                .receivingEmail(dto.isReceivingEmail())
                .build())
        .getId();
  }

  public DictUser findById(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("Unexpected user"));
  }

  public DictUser findByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Unexpected user"));
  }
}
