package com.freeadddictionary.dict.word;

import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.repository.WordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WordInitializer {

  private final WordRepository wordRepository;

  @PostConstruct
  public void init() {
    for (int i = 1; i < 51; i++) {
      Word word =
          Word.builder()
              .name("테스트 단어(" + i + ").")
              .language("영어")
              .partOfSpeech("명사")
              .pronunciation("발음")
              .meaning("뜻")
              .build();
      wordRepository.save(word);
    }
  }
}
