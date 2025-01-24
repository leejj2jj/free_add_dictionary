package com.freeadddictionary.dict.word.dto.response;

import com.freeadddictionary.dict.admin.domain.Admin;
import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.word.domain.Word;
import lombok.Getter;

@Getter
public class WordResponse {

  private String name;
  private String language;
  private String partOfSpeech;
  private String pronunciation;
  private String meaning;
  private User user;
  private Admin admin;

  public WordResponse(Word word) {
    this.name = word.getName();
    this.language = word.getLanguage();
    this.partOfSpeech = word.getPartOfSpeech();
    this.pronunciation = word.getPronunciation();
    this.meaning = word.getMeaning();
    this.user = word.getUser();
    this.admin = word.getAdmin();
  }
}
