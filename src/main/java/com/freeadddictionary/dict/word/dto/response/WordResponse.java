package com.freeadddictionary.dict.word.dto.response;

import com.freeadddictionary.dict.word.domain.Word;
import lombok.Getter;

@Getter
public class WordResponse {

  private String name;
  private String language;
  private String partOfSpeech;
  private String pronunciation;
  private String meaning;

  public WordResponse(Word word) {
    this.name = word.getName();
    this.language = word.getLanguage();
    this.partOfSpeech = word.getPartOfSpeech();
    this.pronunciation = word.getPronunciation();
    this.meaning = word.getMeaning();
  }
}
