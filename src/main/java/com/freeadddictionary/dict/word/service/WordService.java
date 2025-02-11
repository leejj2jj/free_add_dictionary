package com.freeadddictionary.dict.word.service;

import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.repository.UserRepository;
import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.dto.request.WordAddRequest;
import com.freeadddictionary.dict.word.dto.request.WordUpdateRequest;
import com.freeadddictionary.dict.word.exception.WordNotFoundException;
import com.freeadddictionary.dict.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WordService {

  private final WordRepository wordRepository;
  private final UserRepository userRepository;

  @Transactional
  public Word createWord(WordAddRequest request, Long userId) {
    log.info("Creating word: {}", request.getName());
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return wordRepository.save(
        Word.builder()
            .name(request.getName())
            .language(request.getLanguage())
            .partOfSpeech(request.getPartOfSpeech())
            .pronunciation(request.getPronunciation())
            .meaning(request.getMeaning())
            .user(user)
            .build());
  }

  public Page<Word> findAll(Pageable pageable) {
    return wordRepository.findAll(pageable);
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
  public Word update(long id, WordUpdateRequest request) {
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
