package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.annotation.LogExecutionTime;
import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.UserRequest;
import com.freeadddictionary.dict.exception.DuplicateResourceException;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.UserQueryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import com.freeadddictionary.dict.util.LoggingUtil;
import com.freeadddictionary.dict.util.SecurityUtil;
import jakarta.validation.ValidationException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final UserQueryRepository userQueryRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailValidationService emailValidationService;

  public Page<User> searchUsers(String keyword, Pageable pageable) {
    return userQueryRepository.searchByEmailOrNickname(keyword, pageable);
  }

  public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @LogExecutionTime
  @Transactional
  public User createUser(UserRequest request) {
    String email = request.getEmail();

    // 이메일 형식 및 도메인 검증
    if (!emailValidationService.isValidEmail(email)) {
      throw new ValidationException("유효하지 않은 이메일 주소입니다.");
    }

    // 중복 이메일 확인
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateResourceException("User", "email", email);
    }

    User user =
        User.builder()
            .email(email)
            .password(passwordEncoder.encode(request.getPassword()))
            .nickname(request.getNickname())
            .role(Role.USER)
            .build();

    User savedUser = userRepository.save(user);

    LoggingUtil.logAudit("CREATE", "User", savedUser.getId(), "system", Map.of("email", email));

    return savedUser;
  }

  @Transactional
  public User updateUser(Long id, UserRequest request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    String password =
        request.getPassword() != null && !request.getPassword().isEmpty()
            ? passwordEncoder.encode(request.getPassword())
            : user.getPassword();

    user.update(request.getNickname(), password);
    return user;
  }

  @Transactional
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User", "id", id);
    }
    userRepository.deleteById(id);
  }

  public User getUser(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
  }

  @Transactional
  public User promoteToAdmin(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    user.promoteToAdmin();
    return user;
  }

  public Page<User> getAdmins(Pageable pageable) {
    return userRepository.findAllAdmins(pageable);
  }

  @Transactional
  public void unlockUser(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    user.unlock();

    LoggingUtil.logAudit(
        "UNLOCK",
        "User",
        user.getEmail(),
        SecurityUtil.getCurrentUserEmail(),
        Map.of("userId", user.getId()));
  }
}
