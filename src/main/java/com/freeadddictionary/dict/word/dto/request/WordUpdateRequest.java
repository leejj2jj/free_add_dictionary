package com.freeadddictionary.dict.word.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WordUpdateRequest {

  private String name;
  private String language;
  private String partOfSpeech;
  private String pronunciation;
  private String meaning;
}
