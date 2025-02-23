package com.freeadddictionary.dict.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.dto.request.DictionaryRequest;
import com.freeadddictionary.dict.service.DictionaryService;
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
class DictionaryApiControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock private DictionaryService dictionaryService;

  @InjectMocks private DictionaryApiController dictionaryApiController;

  private DictionaryRequest request;
  private Dictionary dictionary;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(dictionaryApiController).build();
    objectMapper = new ObjectMapper();

    request = new DictionaryRequest();
    request.setWord("test");
    request.setLanguage("English");
    request.setPartOfSpeech("noun");
    request.setMeaning("시험");

    dictionary =
        Dictionary.builder()
            .id(1L)
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("시험")
            .build();
  }

  @Test
  @WithMockUser
  void createDictionary_Success() throws Exception {
    given(dictionaryService.createDictionary(any(), any())).willReturn(dictionary);

    mockMvc
        .perform(
            post("/api/dictionary")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/dictionary/1"));

    verify(dictionaryService).createDictionary(any(), any());
  }

  @Test
  @WithMockUser
  void updateDictionary_Success() throws Exception {
    mockMvc
        .perform(
            put("/api/dictionary/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(dictionaryService).updateDictionary(any(), any());
  }

  @Test
  @WithMockUser
  void deleteDictionary_Success() throws Exception {
    mockMvc.perform(delete("/api/dictionary/1").with(csrf())).andExpect(status().isNoContent());

    verify(dictionaryService).deleteDictionary(any());
  }
}
