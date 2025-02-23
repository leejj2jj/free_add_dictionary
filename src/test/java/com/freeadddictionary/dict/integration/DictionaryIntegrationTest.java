package com.freeadddictionary.dict.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.DictionaryRequest;
import com.freeadddictionary.dict.repository.DictionaryRepository;
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
class DictionaryIntegrationTest extends IntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private DictionaryRepository dictionaryRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ObjectMapper objectMapper;

  private User user;
  private Dictionary dictionary;

  @BeforeEach
  protected void setUp() {
    super.setUp();

    user =
        userRepository.save(
            User.builder().email("test@test.com").password("password").nickname("tester").build());

    dictionary =
        dictionaryRepository.save(
            Dictionary.builder()
                .word("test")
                .language("English")
                .partOfSpeech("noun")
                .meaning("시험")
                .user(user)
                .build());
  }

  @Test
  @WithMockUser
  void getDictionary_Success() throws Exception {
    mockMvc
        .perform(get("/dictionary/{id}", dictionary.getId()))
        .andExpect(status().isOk())
        .andExpect(view().name("dictionary/dictionary_detail"))
        .andExpect(model().attributeExists("dictionary"));

    Dictionary found = dictionaryRepository.findById(dictionary.getId()).orElseThrow();
    assertThat(found.getViewCount()).isEqualTo(1);
  }

  @Test
  @WithMockUser
  void createDictionary_Success() throws Exception {
    DictionaryRequest request = new DictionaryRequest();
    request.setWord("new");
    request.setLanguage("English");
    request.setPartOfSpeech("noun");
    request.setMeaning("새로운");

    mockMvc
        .perform(
            post("/api/dictionary")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    assertThat(dictionaryRepository.count()).isEqualTo(2);
  }
}
