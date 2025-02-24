package com.freeadddictionary.dict.dto.request;

import com.freeadddictionary.dict.domain.Dictionary;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DictionaryRequest {
  private Long id;

  @NotBlank(message = "단어를 입력해 주세요")
  @Size(max = 100, message = "단어는 100자를 초과할 수 없습니다")
  private String word;

  @NotBlank(message = "언어를 입력해 주세요")
  @Size(max = 50, message = "언어는 50자를 초과할 수 없습니다")
  private String language;

  @NotBlank(message = "품사를 입력해 주세요")
  @Size(max = 50, message = "품사는 50자를 초과할 수 없습니다")
  private String partOfSpeech;

  @Size(max = 100, message = "발음은 100자를 초과할 수 없습니다")
  private String pronunciation;

  @NotBlank(message = "의미를 입력해 주세요")
  @Size(max = 1000, message = "의미는 1000자를 초과할 수 없습니다")
  private String meaning;

  @Size(max = 1000, message = "예문은 1000자를 초과할 수 없습니다")
  private String exampleSentence;

  public DictionaryRequest(Dictionary dictionary) {
    this.id = dictionary.getId();
    this.word = dictionary.getWord();
    this.language = dictionary.getLanguage();
    this.partOfSpeech = dictionary.getPartOfSpeech();
    this.pronunciation = dictionary.getPronunciation();
    this.meaning = dictionary.getMeaning();
    this.exampleSentence = dictionary.getExampleSentence();
  }
}
