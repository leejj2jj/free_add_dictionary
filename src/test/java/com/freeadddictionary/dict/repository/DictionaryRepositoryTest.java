package com.freeadddictionary.dict.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class DictionaryRepositoryTest {

  @Autowired private DictionaryRepository dictionaryRepository;

  @Autowired private UserRepository userRepository;

  private User user;
  private Dictionary dictionary;

  @BeforeEach
  void setUp() {
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
  void getRecentDictionaries_Success() {
    var result =
        dictionaryRepository.findRecentDictionaries(java.time.LocalDateTime.now().minusDays(1));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getWord()).isEqualTo("test");
  }

  @Test
  void getPopularDictionaries_Success() {
    dictionary.incrementViewCount();
    dictionaryRepository.save(dictionary);

    var result = dictionaryRepository.findPopularDictionaries(PageRequest.of(0, 10));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getViewCount()).isEqualTo(1);
  }

  @Test
  void searchByKeyword_Success() {
    var pageable = PageRequest.of(0, 10, Sort.by("word").ascending());

    Page<Dictionary> result = dictionaryRepository.searchByKeyword("test", pageable);

    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).getWord()).isEqualTo("test");
  }
}
