package com.freeadddictionary.dict.word.dto.response;

import com.freeadddictionary.dict.admin.domain.Admin;
import com.freeadddictionary.dict.user.domain.User;
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
  private LocalDateTime createdAt;
  private LocalDateTime lastModifiedAt;
  private User user;
  private Admin admin;

  public WordViewResponse(Word word) {
    this.id = word.getId();
    this.name = word.getName();
    this.language = word.getLanguage();
    this.partOfSpeech = word.getPartOfSpeech();
    this.pronunciation = word.getPronunciation();
    this.meaning = word.getMeaning();
    this.createdAt = word.getCreatedAt();
    this.lastModifiedAt = word.getLastModifiedAt();
    this.user = word.getUser();
    this.admin = word.getAdmin();
  }
}
