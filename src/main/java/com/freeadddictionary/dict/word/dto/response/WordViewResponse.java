package com.freeadddictionary.dict.word.dto.response;

import com.freeadddictionary.dict.admin.domain.Admin;
import com.freeadddictionary.dict.member.domain.Member;
import com.freeadddictionary.dict.word.domain.Word;
import java.time.LocalDateTime;
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
  private Member member;
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
    this.member = word.getMember();
    this.admin = word.getAdmin();
  }
}
