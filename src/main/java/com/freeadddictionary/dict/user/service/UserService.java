package com.freeadddictionary.dict.user.service;

import com.freeadddictionary.dict.shared.exception.DataNotFoundException;
import com.freeadddictionary.dict.user.domain.DictUser;
import com.freeadddictionary.dict.user.dto.request.UserRegisterRequest;
import com.freeadddictionary.dict.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public DictUser create(UserRegisterRequest request) {
    DictUser user =
        DictUser.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword1()))
            .username(request.getUsername())
            .phone(request.getPhone())
            .receivingEmail(request.isReceivingEmail())
            .build();
    userRepository.save(user);
    return user;
  }

  public DictUser getUserByEmail(String email) {
    Optional<DictUser> user = userRepository.findByEmail(email);
    if (user.isPresent()) {
      return user.get();
    } else {
      throw new DataNotFoundException("Dict user not found with email " + email);
    }
  }

  public DictUser getUserById(Long id) {
    Optional<DictUser> user = userRepository.findById(id);
    if (user.isPresent()) {
      return user.get();
    } else {
      throw new DataNotFoundException("Dict user not found with id: " + id);
    }
  }
}
