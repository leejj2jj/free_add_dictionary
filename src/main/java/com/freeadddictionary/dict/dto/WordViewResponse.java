package com.freeadddictionary.dict.dto;

import java.time.LocalDateTime;

import com.freeadddictionary.dict.domain.Admin;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.domain.Word;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WordViewResponse {

  private Long id;
  private String name;
  private String language;
  private String partOfSpeech;
  private String pronunciation;
  private String meaning;
  private LocalDateTime addDate;
  private LocalDateTime modifyDate;
  private User user;
  private Admin admin;

  public WordViewResponse(Word word) {
    this.id = word.getId();
    this.name = word.getName();
    this.language = word.getLanguage();
    this.partOfSpeech = word.getPartOfSpeech();
    this.pronunciation = word.getPronunciation();
    this.meaning = word.getMeaning();
    this.addDate = word.getAddDate();
    this.modifyDate = word.getModifyDate();
    this.user = word.getUser();
    this.admin = word.getAdmin();
  }

}
