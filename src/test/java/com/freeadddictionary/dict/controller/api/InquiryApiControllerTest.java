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
import com.freeadddictionary.dict.domain.Inquiry;
import com.freeadddictionary.dict.dto.request.InquiryRequest;
import com.freeadddictionary.dict.service.InquiryService;
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
class InquiryApiControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock private InquiryService inquiryService;

  @InjectMocks private InquiryApiController inquiryApiController;

  private InquiryRequest request;
  private Inquiry inquiry;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(inquiryApiController).build();
    objectMapper = new ObjectMapper();

    request = new InquiryRequest();
    request.setTitle("Test Inquiry");
    request.setContent("Test Content");

    inquiry =
        Inquiry.builder()
            .id(1L)
            .title("Test Inquiry")
            .content("Test Content")
            .authorEmail("test@test.com")
            .build();
  }

  @Test
  @WithMockUser
  void createInquiry_Success() throws Exception {
    given(inquiryService.createInquiry(any(), any())).willReturn(inquiry);

    mockMvc
        .perform(
            post("/api/inquiry")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/inquiry/1"));

    verify(inquiryService).createInquiry(any(), any());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void resolveInquiry_Success() throws Exception {
    mockMvc.perform(post("/api/inquiry/1/resolve").with(csrf())).andExpect(status().isOk());

    verify(inquiryService).resolveInquiry(any());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteInquiry_Success() throws Exception {
    mockMvc.perform(delete("/api/inquiry/1").with(csrf())).andExpect(status().isNoContent());

    verify(inquiryService).deleteInquiry(any());
  }
}
