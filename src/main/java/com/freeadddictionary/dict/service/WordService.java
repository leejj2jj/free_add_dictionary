package com.freeadddictionary.dict.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.freeadddictionary.dict.domain.Word;
import com.freeadddictionary.dict.dto.AddWordRequest;
import com.freeadddictionary.dict.dto.UpdateWordRequest;
import com.freeadddictionary.dict.repository.WordRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
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
    return wordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Word not found: " + id));
  }

  public void delete(long id) {
    wordRepository.deleteById(id);
  }

  @Transactional
  public Word update(long id, UpdateWordRequest request) {
    Word word = wordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Word not found: " + id));

    word.update(request.getName(), request.getLanguage(), request.getPartOfSpeech(), request.getPronunciation(),
        request.getMeaning());
    return word;
  }
}
