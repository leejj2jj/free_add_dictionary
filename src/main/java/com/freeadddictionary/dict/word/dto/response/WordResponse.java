package com.freeadddictionary.dict.word.dto.response;

import com.freeadddictionary.dict.admin.domain.Admin;
import com.freeadddictionary.dict.member.domain.Member;
import com.freeadddictionary.dict.word.domain.Word;

import lombok.Getter;

@Getter
public class WordResponse {

  private String name;
  private String language;
  private String partOfSpeech;
  private String pronunciation;
  private String meaning;
  private Member member;
  private Admin admin;

  public WordResponse(Word word) {
    this.name = word.getName();
    this.language = word.getLanguage();
    this.partOfSpeech = word.getPartOfSpeech();
    this.pronunciation = word.getPronunciation();
    this.meaning = word.getMeaning();
    this.member = word.getMember();
    this.admin = word.getAdmin();
  }

}
