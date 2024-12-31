package com.freeadddictionary.dict.word.dto;

import com.freeadddictionary.dict.word.domain.Word;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddWordRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String partOfSpeech;

  @NotBlank
  private String pronunciation;

  @NotBlank
  private String meaning;

  public Word toEntity() {
    return Word.builder()
        .name(name)
        .partOfSpeech(partOfSpeech)
        .pronunciation(pronunciation)
        .meaning(meaning)
        .build();
  }

}