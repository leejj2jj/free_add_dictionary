package com.freeadddictionary.dict.dto.response;

import com.freeadddictionary.dict.domain.Dictionary;
import java.time.LocalDateTime;

public record DictionaryResponse(
    Long id,
    String word,
    String language,
    String partOfSpeech,
    String pronunciation,
    String meaning,
    String exampleSentence,
    LocalDateTime createdAt) {

  public static DictionaryResponse from(Dictionary dictionary) {
    return new DictionaryResponse(
        dictionary.getId(),
        dictionary.getWord(),
        dictionary.getLanguage(),
        dictionary.getPartOfSpeech(),
        dictionary.getPronunciation(),
        dictionary.getMeaning(),
        dictionary.getExampleSentence(),
        dictionary.getCreatedAt());
  }
}
