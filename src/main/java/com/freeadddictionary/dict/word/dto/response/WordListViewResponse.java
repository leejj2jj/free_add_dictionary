package com.freeadddictionary.dict.word.dto.response;

import com.freeadddictionary.dict.word.domain.Word;
import lombok.Getter;

@Getter
public class WordListViewResponse {

  private final Long id;
  private final String name;
  private final String language;
  private final String meaning;

  public WordListViewResponse(Word word) {
    this.id = word.getId();
    this.name = word.getName();
    this.language = word.getLanguage();
    this.meaning = word.getMeaning();
  }
}
