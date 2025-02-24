package com.freeadddictionary.dict.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.DictionaryRequest;
import com.freeadddictionary.dict.exception.DuplicateResourceException;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.DictionaryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DictionaryServiceTest {

  @Mock private DictionaryRepository dictionaryRepository;

  @Mock private UserRepository userRepository;

  @InjectMocks private DictionaryService dictionaryService;

  private DictionaryRequest request;
  private User user;
  private Dictionary dictionary;

  @BeforeEach
  void setUp() {
    user = User.builder().email("test@test.com").password("password").nickname("tester").build();

    request = new DictionaryRequest();
    request.setWord("test");
    request.setLanguage("English");
    request.setPartOfSpeech("noun");
    request.setMeaning("시험");

    dictionary =
        Dictionary.builder()
            .word(request.getWord())
            .language(request.getLanguage())
            .partOfSpeech(request.getPartOfSpeech())
            .meaning(request.getMeaning())
            .user(user)
            .build();
  }

  @Test
  void createDictionary_Success() {
    given(dictionaryRepository.existsByWord(request.getWord())).willReturn(false);
    given(userRepository.findByEmail(user.getEmail())).willReturn(java.util.Optional.of(user));
    given(dictionaryRepository.save(any(Dictionary.class))).willReturn(dictionary);

    Dictionary created = dictionaryService.createDictionary(request, user.getEmail());

    assertThat(created).isNotNull();
    assertThat(created.getWord()).isEqualTo(request.getWord());
    verify(dictionaryRepository).save(any(Dictionary.class));
  }

  @Test
  void createDictionary_DuplicateWord() {
    given(dictionaryRepository.existsByWord(request.getWord())).willReturn(true);

    assertThatThrownBy(() -> dictionaryService.createDictionary(request, user.getEmail()))
        .isInstanceOf(DuplicateResourceException.class);
  }

  @Test
  void createDictionary_UserNotFound() {
    given(dictionaryRepository.existsByWord(request.getWord())).willReturn(false);
    given(userRepository.findByEmail(user.getEmail())).willReturn(java.util.Optional.empty());

    assertThatThrownBy(() -> dictionaryService.createDictionary(request, user.getEmail()))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
