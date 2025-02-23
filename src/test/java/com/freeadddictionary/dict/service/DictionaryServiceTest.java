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
import java.util.Optional;
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

  private User user;
  private Dictionary dictionary;
  private DictionaryRequest request;

  @BeforeEach
  void setUp() {
    user = User.builder().email("test@test.com").password("password").nickname("tester").build();

    dictionary =
        Dictionary.builder()
            .word("test")
            .language("English")
            .partOfSpeech("noun")
            .meaning("시험")
            .user(user)
            .build();

    request = new DictionaryRequest();
    request.setWord("test");
    request.setLanguage("English");
    request.setPartOfSpeech("noun");
    request.setMeaning("시험");
  }

  @Test
  void createDictionary_Success() {
    // given
    given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
    given(dictionaryRepository.existsByWord(any())).willReturn(false);
    given(dictionaryRepository.save(any())).willReturn(dictionary);

    // when
    Dictionary result = dictionaryService.createDictionary(request, "test@test.com");

    // then
    assertThat(result.getWord()).isEqualTo(request.getWord());
    verify(dictionaryRepository).save(any());
  }

  @Test
  void createDictionary_DuplicateWord() {
    // given
    given(dictionaryRepository.existsByWord(any())).willReturn(true);

    // then
    assertThatThrownBy(() -> dictionaryService.createDictionary(request, "test@test.com"))
        .isInstanceOf(DuplicateResourceException.class);
  }

  @Test
  void getDictionary_Success() {
    // given
    given(dictionaryRepository.findById(any())).willReturn(Optional.of(dictionary));

    // when
    Dictionary result = dictionaryService.getDictionary(1L);

    // then
    assertThat(result).isEqualTo(dictionary);
  }

  @Test
  void getDictionary_NotFound() {
    // given
    given(dictionaryRepository.findById(any())).willReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> dictionaryService.getDictionary(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
