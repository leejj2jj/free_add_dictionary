package com.freeadddictionary.dict.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.Report;
import com.freeadddictionary.dict.dto.request.ReportRequest;
import com.freeadddictionary.dict.service.ReportService;
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
class ReportApiControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock private ReportService reportService;

  @InjectMocks private ReportApiController reportApiController;

  private ReportRequest request;
  private Report report;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(reportApiController).build();
    objectMapper = new ObjectMapper();

    request = new ReportRequest();
    request.setTitle("Test Report");
    request.setContent("Test Content");

    report =
        Report.builder()
            .id(1L)
            .title("Test Report")
            .content("Test Content")
            .authorEmail("test@test.com")
            .build();
  }

  @Test
  @WithMockUser
  void createReport_Success() throws Exception {
    given(reportService.createReport(any(), any())).willReturn(report);

    mockMvc
        .perform(
            post("/api/report")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/report/1"));

    verify(reportService).createReport(any(), any());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void resolveReport_Success() throws Exception {
    mockMvc.perform(post("/api/report/1/resolve").with(csrf())).andExpect(status().isOk());

    verify(reportService).resolveReport(any());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteReport_Success() throws Exception {
    mockMvc.perform(delete("/api/report/1").with(csrf())).andExpect(status().isNoContent());

    verify(reportService).deleteReport(any());
  }
}
