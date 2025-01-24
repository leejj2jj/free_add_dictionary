package com.freeadddictionary.dict.config.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.config.jwt.JwtFactory;
import com.freeadddictionary.dict.user.config.jwt.JwtProperties;
import com.freeadddictionary.dict.user.config.jwt.domain.RefreshToken;
import com.freeadddictionary.dict.user.config.jwt.dto.CreateAccessTokenRequest;
import com.freeadddictionary.dict.user.config.jwt.repository.RefreshTokenRepository;
import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.repository.UserRepository;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiControllerTest {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @Autowired private WebApplicationContext context;

  @Autowired JwtProperties jwtProperties;

  @Autowired UserRepository userRepository;

  @Autowired RefreshTokenRepository refreshTokenRepository;

  @BeforeEach
  public void mockMvcSetup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    userRepository.deleteAll();
  }

  @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
  @Test
  public void createNewAccessToken() throws Exception {
    // given
    final String url = "/api/token";

    User testUser =
        userRepository.save(
            User.builder()
                .email("user@gmail.com")
                .password("test")
                .name("test")
                .phone("010-1234-5678")
                .receivingEmail(true)
                .build());

    String refreshToken =
        JwtFactory.builder()
            .claims(Map.of("id", testUser.getId()))
            .build()
            .createToken(jwtProperties);
    refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

    CreateAccessTokenRequest request = new CreateAccessTokenRequest();
    request.setRefreshToken(refreshToken);
    final String requestBody = objectMapper.writeValueAsString(request);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty());
  }
}
