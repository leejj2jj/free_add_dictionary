package com.freeadddictionary.dict.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.freeadddictionary.dict.domain.Inquiry;
import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.repository.InquiryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import com.freeadddictionary.dict.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class AdminIntegrationTest extends IntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private InquiryRepository inquiryRepository;

  private User admin;
  private Inquiry inquiry;

  @BeforeEach
  protected void setUp() {
    super.setUp();

    admin =
        userRepository.save(
            User.builder()
                .email("admin@test.com")
                .password("password")
                .nickname("admin")
                .role(Role.ADMIN)
                .build());

    inquiry =
        inquiryRepository.save(
            Inquiry.builder()
                .title("Test Inquiry")
                .content("Test Content")
                .authorEmail("user@test.com")
                .user(admin)
                .build());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getDashboard_Success() throws Exception {
    mockMvc
        .perform(get("/admin"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/admin_dashboard"))
        .andExpect(model().attributeExists("statistics"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getInquiries_Success() throws Exception {
    mockMvc
        .perform(get("/admin/inquiries"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/admin_inquiries"))
        .andExpect(model().attributeExists("inquiries"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void getDashboard_Forbidden() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
  }
}
