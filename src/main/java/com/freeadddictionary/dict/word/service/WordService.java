package com.freeadddictionary.dict.word.service;

import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.dto.request.AddWordRequest;
import com.freeadddictionary.dict.word.dto.request.UpdateWordRequest;
import com.freeadddictionary.dict.word.exception.WordNotFoundException;
import com.freeadddictionary.dict.word.repository.WordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WordService {

  private final WordRepository wordRepository;

  public Word save(AddWordRequest word) {
    return wordRepository.save(word.toEntity());
  }

  public List<Word> findAll() {
    return wordRepository.findAll();
  }

  public Word findById(long id) {
    return wordRepository
        .findById(id)
        .orElseThrow(() -> new WordNotFoundException("Word not found: " + id));
  }

  public void delete(long id) {
    wordRepository.deleteById(id);
  }

  @Transactional
  public Word update(long id, UpdateWordRequest request) {
    Word word =
        wordRepository
            .findById(id)
            .orElseThrow(() -> new WordNotFoundException("Word not found: " + id));

    word.update(
        request.getName(),
        request.getLanguage(),
        request.getPartOfSpeech(),
        request.getPronunciation(),
        request.getMeaning());
    return word;
  }
}
