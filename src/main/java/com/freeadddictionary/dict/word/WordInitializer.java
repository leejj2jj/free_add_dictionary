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
    Word word =
        Word.builder()
            .name("word")
            .language("영어")
            .partOfSpeech("명사")
            .pronunciation("워드")
            .meaning("단어")
            .build();

    Word airplane =
        Word.builder()
            .name("airplane")
            .language("영어")
            .partOfSpeech("명사")
            .pronunciation("에어플레인")
            .meaning("비행기")
            .build();

    Word forest =
        Word.builder()
            .name("forest")
            .language("영어")
            .partOfSpeech("명사")
            .pronunciation("포레스트")
            .meaning("숲")
            .build();

    Word coffee =
        Word.builder()
            .name("coffee")
            .language("영어")
            .partOfSpeech("명사")
            .pronunciation("커피")
            .meaning("커피")
            .build();

    Word dictionary =
        Word.builder()
            .name("dictionary")
            .language("영어")
            .partOfSpeech("명사")
            .pronunciation("딕셔너리")
            .meaning("사전")
            .build();

    wordRepository.save(word);
    wordRepository.save(airplane);
    wordRepository.save(forest);
    wordRepository.save(coffee);
    wordRepository.save(dictionary);
  }
}
