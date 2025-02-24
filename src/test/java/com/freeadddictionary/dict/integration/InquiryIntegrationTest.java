package com.freeadddictionary.dict.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.Inquiry;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.InquiryRequest;
import com.freeadddictionary.dict.repository.InquiryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import com.freeadddictionary.dict.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class InquiryIntegrationTest extends IntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private InquiryRepository inquiryRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ObjectMapper objectMapper;

  private User user;
  private Inquiry inquiry;

  @BeforeEach
  protected void setUp() {
    super.setUp();

    user =
        userRepository.save(
            User.builder().email("test@test.com").password("password").nickname("tester").build());

    inquiry =
        inquiryRepository.save(
            Inquiry.builder()
                .title("Test Inquiry")
                .content("Test Content")
                .authorEmail("test@test.com")
                .user(user)
                .build());
  }

  @Test
  @WithMockUser
  void createInquiry_Success() throws Exception {
    InquiryRequest request = new InquiryRequest();
    request.setTitle("New Inquiry");
    request.setContent("New Content");

    mockMvc
        .perform(
            post("/api/inquiry")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    assertThat(inquiryRepository.count()).isEqualTo(2);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void resolveInquiry_Success() throws Exception {
    mockMvc
        .perform(post("/api/inquiry/{id}/resolve", inquiry.getId()).with(csrf()))
        .andExpect(status().isOk());

    Inquiry resolvedInquiry = inquiryRepository.findById(inquiry.getId()).orElseThrow();
    assertThat(resolvedInquiry.isResolved()).isTrue();
  }
}
