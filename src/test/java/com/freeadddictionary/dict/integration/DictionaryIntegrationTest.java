package com.freeadddictionary.dict.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.DictionaryRequest;
import com.freeadddictionary.dict.repository.DictionaryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import com.freeadddictionary.dict.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

class DictionaryIntegrationTest extends IntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private DictionaryRepository dictionaryRepository;

  @Autowired private UserRepository userRepository;

  private User testUser;

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
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void createDictionary_Success() throws Exception {
    DictionaryRequest request =
        DictionaryRequest.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .build();

    mockMvc
        .perform(
            post("/api/dictionary")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void getDictionary_Success() throws Exception {
    Dictionary dictionary =
        Dictionary.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .pronunciation("test")
            .exampleSentence("This is a test.")
            .user(testUser)
            .build();
    dictionaryRepository.save(dictionary);

    mockMvc
        .perform(get("/dictionary/{id}", dictionary.getId()))
        .andExpect(status().isOk())
        .andExpect(view().name("dictionary/dictionary_detail"))
        .andExpect(model().attributeExists("dictionary"));
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void updateDictionary_Success() throws Exception {
    Dictionary dictionary =
        Dictionary.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .user(testUser)
            .build();
    dictionaryRepository.save(dictionary);

    DictionaryRequest request =
        DictionaryRequest.builder()
            .word("updated")
            .language("Korean")
            .partOfSpeech("verb")
            .meaning("updated meaning")
            .build();

    mockMvc
        .perform(
            put("/api/dictionary/{id}", dictionary.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void deleteDictionary_Success() throws Exception {
    Dictionary dictionary =
        Dictionary.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .user(testUser)
            .build();
    dictionaryRepository.save(dictionary);

    mockMvc
        .perform(delete("/api/dictionary/{id}", dictionary.getId()).with(csrf()))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void listDictionaries_Success() throws Exception {
    Dictionary dictionary =
        Dictionary.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .user(testUser)
            .build();
    dictionaryRepository.save(dictionary);

    mockMvc
        .perform(get("/dictionary"))
        .andExpect(status().isOk())
        .andExpect(view().name("dictionary/dictionary_list"))
        .andExpect(model().attributeExists("dictionaries"));
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void searchDictionaries_Success() throws Exception {
    Dictionary dictionary =
        Dictionary.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .user(testUser)
            .build();
    dictionaryRepository.save(dictionary);

    mockMvc
        .perform(get("/dictionary").param("keyword", "test"))
        .andExpect(status().isOk())
        .andExpect(view().name("dictionary/dictionary_list"))
        .andExpect(model().attributeExists("dictionaries"))
        .andExpect(model().attributeExists("keyword"));
  }

  @Test
  @WithMockUser(username = "other@example.com")
  void updateDictionary_Forbidden() throws Exception {
    Dictionary dictionary =
        Dictionary.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .user(testUser)
            .build();
    dictionaryRepository.save(dictionary);

    DictionaryRequest request =
        DictionaryRequest.builder()
            .word("updated")
            .language("Korean")
            .partOfSpeech("verb")
            .meaning("updated meaning")
            .build();

    mockMvc
        .perform(
            put("/api/dictionary/{id}", dictionary.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void getDictionary_UnauthorizedAccess() throws Exception {
    Dictionary dictionary =
        Dictionary.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .user(testUser)
            .build();
    dictionaryRepository.save(dictionary);

    mockMvc.perform(get("/dictionary/{id}", dictionary.getId())).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void createDictionary_InvalidInput() throws Exception {
    DictionaryRequest request =
        DictionaryRequest.builder()
            .word("") // empty word
            .language("English")
            .partOfSpeech("noun")
            .meaning("test meaning")
            .build();

    mockMvc
        .perform(
            post("/api/dictionary")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
