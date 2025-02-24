package com.freeadddictionary.dict.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.Inquiry;
import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.InquiryRequest;
import com.freeadddictionary.dict.repository.InquiryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import com.freeadddictionary.dict.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

class InquiryIntegrationTest extends IntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private InquiryRepository inquiryRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ObjectMapper objectMapper;

  private User testUser;
  private User adminUser;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    testUser =
        userRepository.save(
            User.builder()
                .email("test@example.com")
                .password("password")
                .nickname("tester")
                .role(Role.USER)
                .build());

    adminUser =
        userRepository.save(
            User.builder()
                .email("admin@example.com")
                .password("password")
                .nickname("admin")
                .role(Role.ADMIN)
                .build());
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void createInquiry_Success() throws Exception {
    InquiryRequest request =
        InquiryRequest.builder().title("Test Inquiry").content("Test Content").build();

    mockMvc
        .perform(
            post("/api/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

  @Test
  @WithMockUser(username = "admin@example.com", roles = "ADMIN")
  void resolveInquiry_Success() throws Exception {
    Inquiry inquiry =
        inquiryRepository.save(
            Inquiry.builder()
                .title("Test Inquiry")
                .content("Test Content")
                .authorEmail(testUser.getEmail())
                .user(testUser)
                .build());

    mockMvc
        .perform(post("/api/inquiry/{id}/resolve", inquiry.getId()).with(csrf()))
        .andExpect(status().isOk());

    Inquiry resolvedInquiry = inquiryRepository.findById(inquiry.getId()).orElseThrow();
    assertThat(resolvedInquiry.isResolved()).isTrue();
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void getUserInquiries_Success() throws Exception {
    inquiryRepository.save(
        Inquiry.builder()
            .title("Test Inquiry")
            .content("Test Content")
            .authorEmail(testUser.getEmail())
            .user(testUser)
            .build());

    mockMvc
        .perform(get("/inquiry"))
        .andExpect(status().isOk())
        .andExpect(view().name("inquiry/inquiry_list"))
        .andExpect(model().attributeExists("inquiries"));
  }

  @Test
  @WithMockUser(username = "admin@example.com", roles = "ADMIN")
  void getAdminInquiries_Success() throws Exception {
    mockMvc
        .perform(get("/admin/inquiries"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/admin_inquiries"))
        .andExpect(model().attributeExists("inquiries"));
  }
}
