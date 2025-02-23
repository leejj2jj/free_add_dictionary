package com.freeadddictionary.dict.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.UserRequest;
import com.freeadddictionary.dict.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserApiControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock private UserService userService;

  @InjectMocks private UserApiController userApiController;

  private UserRequest request;
  private User user;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userApiController).build();
    objectMapper = new ObjectMapper();

    request = new UserRequest();
    request.setEmail("test@test.com");
    request.setPassword("password");
    request.setNickname("tester");

    user =
        User.builder()
            .id(1L)
            .email("test@test.com")
            .password("encodedPassword")
            .nickname("tester")
            .build();
  }

  @Test
  void register_Success() throws Exception {
    given(userService.createUser(any())).willReturn(user);

    mockMvc
        .perform(
            post("/api/user/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/user/1"));

    verify(userService).createUser(any());
  }

  @Test
  @WithMockUser
  void update_Success() throws Exception {
    mockMvc
        .perform(
            put("/api/user/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(userService).updateUser(any(), any());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void promoteToAdmin_Success() throws Exception {
    mockMvc.perform(post("/api/user/1/promote").with(csrf())).andExpect(status().isOk());

    verify(userService).promoteToAdmin(any());
  }
}
