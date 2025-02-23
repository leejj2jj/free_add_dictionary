package com.freeadddictionary.dict.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.UserRequest;
import com.freeadddictionary.dict.exception.DuplicateResourceException;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserService userService;

  private User user;
  private UserRequest request;

  @BeforeEach
  void setUp() {
    user =
        User.builder()
            .email("test@test.com")
            .password("encodedPassword")
            .nickname("tester")
            .role(Role.USER)
            .build();

    request = new UserRequest();
    request.setEmail("test@test.com");
    request.setPassword("password");
    request.setNickname("tester");
  }

  @Test
  void createUser_Success() {
    // given
    given(userRepository.existsByEmail(any())).willReturn(false);
    given(passwordEncoder.encode(any())).willReturn("encodedPassword");
    given(userRepository.save(any())).willReturn(user);

    // when
    User result = userService.createUser(request);

    // then
    assertThat(result.getEmail()).isEqualTo(request.getEmail());
    verify(userRepository).save(any());
  }

  @Test
  void createUser_DuplicateEmail() {
    // given
    given(userRepository.existsByEmail(any())).willReturn(true);

    // then
    assertThatThrownBy(() -> userService.createUser(request))
        .isInstanceOf(DuplicateResourceException.class);
  }

  @Test
  void promoteToAdmin_Success() {
    // given
    given(userRepository.findById(any())).willReturn(Optional.of(user));

    // when
    User result = userService.promoteToAdmin(1L);

    // then
    assertThat(result.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  void promoteToAdmin_UserNotFound() {
    // given
    given(userRepository.findById(any())).willReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> userService.promoteToAdmin(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
