package com.freeadddictionary.dict.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.Report;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.ReportRequest;
import com.freeadddictionary.dict.repository.ReportRepository;
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
class ReportIntegrationTest extends IntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ReportRepository reportRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ObjectMapper objectMapper;

  private User user;
  private Report report;

  @BeforeEach
  protected void setUp() {
    super.setUp();

    user =
        userRepository.save(
            User.builder().email("test@test.com").password("password").nickname("tester").build());

    report =
        reportRepository.save(
            Report.builder()
                .title("Test Report")
                .content("Test Content")
                .authorEmail("test@test.com")
                .user(user)
                .build());
  }

  @Test
  @WithMockUser
  void createReport_Success() throws Exception {
    ReportRequest request = new ReportRequest();
    request.setTitle("New Report");
    request.setContent("New Content");

    mockMvc
        .perform(
            post("/api/report")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    assertThat(reportRepository.count()).isEqualTo(2);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void resolveReport_Success() throws Exception {
    mockMvc
        .perform(post("/api/report/{id}/resolve", report.getId()).with(csrf()))
        .andExpect(status().isOk());

    Report resolvedReport = reportRepository.findById(report.getId()).orElseThrow();
    assertThat(resolvedReport.isResolved()).isTrue();
  }
}
