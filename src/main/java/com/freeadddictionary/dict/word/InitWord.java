package com.freeadddictionary.dict.word;

import com.freeadddictionary.dict.word.domain.Word;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitWord {

  private final InitWordService initWordService;

  @PostConstruct
  public void init() {
    initWordService.init();
  }

  @Component
  static class InitWordService {

    private EntityManager em;

    public InitWordService(EntityManager em) {
      this.em = em;
    }

    @Transactional
    public void init() {
      for (int i = 1; i < 51; i++) {
        em.persist(
            Word.builder()
                .name("테스트 단어(" + i + ").")
                .language("영어")
                .partOfSpeech("명사")
                .pronunciation("발음")
                .meaning("뜻")
                .build());
      }
    }
  }
}
