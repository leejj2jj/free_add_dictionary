package com.freeadddictionary.dict.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.UserRequest;
import com.freeadddictionary.dict.repository.UserRepository;
import com.freeadddictionary.dict.support.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

class UserIntegrationTest extends IntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void register_Success() throws Exception {
    UserRequest request = new UserRequest();
    request.setEmail("test@test.com");
    request.setPassword("password123");
    request.setNickname("tester");

    mockMvc
        .perform(
            post("/api/user/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    User user = userRepository.findByEmail("test@test.com").orElseThrow();
    assertThat(user.getNickname()).isEqualTo("tester");
    assertThat(passwordEncoder.matches("password123", user.getPassword())).isTrue();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void promoteToAdmin_Success() throws Exception {
    User user =
        userRepository.save(
            User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("password"))
                .nickname("tester")
                .role(Role.USER)
                .build());

    mockMvc
        .perform(post("/api/user/{id}/promote", user.getId()).with(csrf()))
        .andExpect(status().isOk());

    User updatedUser = userRepository.findById(user.getId()).orElseThrow();
    assertThat(updatedUser.getRole()).isEqualTo(Role.ADMIN);
  }
}
