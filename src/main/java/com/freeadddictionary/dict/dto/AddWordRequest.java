package com.freeadddictionary.dict.dto;

import com.freeadddictionary.dict.domain.Word;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddWordRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String language;

  @NotBlank
  private String partOfSpeech;

  @NotBlank
  private String pronunciation;

  @NotBlank
  private String meaning;

  public Word toEntity() {
    return Word.builder().name(name).language(language).partOfSpeech(partOfSpeech).pronunciation(pronunciation)
        .meaning(meaning).build();
  }

}
