package com.freeadddictionary.dict.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWordRequest {

  private String name;
  private String language;
  private String partOfSpeech;
  private String pronunciation;
  private String meaning;
}
