package com.freeadddictionary.dict.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.dto.request.RegisterUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

  @Autowired UserService userService;
  @Autowired UserDetailService userDetailService;
  @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;

  @Test
  public void testUserRegistration() {
    RegisterUserRequest dto =
        new RegisterUserRequest("test@example.com", "password123", "Test", "010-1234-5678", true);

    Long userId = userService.save(dto);

    User user = userService.findById(userId);
    assertNotNull(user);
    assertEquals("test@example.com", user.getEmail());
    assertTrue(bCryptPasswordEncoder.matches("password123", user.getPassword()));

    UserDetails userDetails = userDetailService.loadUserByUsername("test@example.com");
    assertNotNull(userDetails);
    assertEquals("test@example.com", userDetails.getUsername());
    assertTrue(bCryptPasswordEncoder.matches("password123", userDetails.getPassword()));
  }
}
