package com.freeadddictionary.dict.word.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WordUpdateRequest {

  @NotBlank(message = "단어를 입력해 주세요.")
  private String name;

  @NotBlank(message = "단어의 언어를 입력해 주세요.")
  private String language;

  @NotBlank(message = "단어의 품사를 입력해 주세요.")
  private String partOfSpeech;

  @NotBlank(message = "단어의 발음을 입력해 주세요.")
  private String pronunciation;

  @NotBlank(message = "단어의 뜻을 입력해 주세요.")
  private String meaning;
}
