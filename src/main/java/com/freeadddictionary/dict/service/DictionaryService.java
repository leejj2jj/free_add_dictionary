package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.DictionaryRequest;
import com.freeadddictionary.dict.exception.DuplicateResourceException;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.DictionaryQueryRepository;
import com.freeadddictionary.dict.repository.DictionaryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DictionaryService {

  private final DictionaryRepository dictionaryRepository;
  private final DictionaryQueryRepository dictionaryQueryRepository;
  private final UserRepository userRepository;

  public Page<Dictionary> searchDictionaries(String keyword, Pageable pageable) {
    return dictionaryQueryRepository.searchDictionaries(keyword, pageable);
  }

  @Transactional
  public Dictionary createDictionary(DictionaryRequest request, String email) {
    if (dictionaryRepository.existsByWord(request.getWord())) {
      throw new DuplicateResourceException("Dictionary", "word", request.getWord());
    }

    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

    Dictionary dictionary =
        Dictionary.builder()
            .word(request.getWord())
            .language(request.getLanguage())
            .partOfSpeech(request.getPartOfSpeech())
            .pronunciation(request.getPronunciation())
            .meaning(request.getMeaning())
            .exampleSentence(request.getExampleSentence())
            .user(user)
            .build();

    return dictionaryRepository.save(dictionary);
  }

  @Transactional
  public Dictionary updateDictionary(Long id, DictionaryRequest request) {
    Dictionary dictionary =
        dictionaryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));

    dictionary.update(
        request.getLanguage(),
        request.getPartOfSpeech(),
        request.getPronunciation(),
        request.getMeaning(),
        request.getExampleSentence());

    return dictionary;
  }

  @Transactional
  public void deleteDictionary(Long id) {
    if (!dictionaryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Dictionary", "id", id);
    }
    dictionaryRepository.deleteById(id);
  }

  public Dictionary getDictionary(Long id) {
    return dictionaryRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));
  }

  public List<Dictionary> getRecentDictionaries() {
    LocalDateTime startTime = LocalDateTime.now().minusDays(7);
    return dictionaryRepository.findRecentDictionaries(startTime);
  }

  public List<Dictionary> getPopularDictionaries() {
    return dictionaryRepository.findPopularDictionaries(Pageable.ofSize(10));
  }

  @Transactional
  public void incrementViewCount(Long id) {
    Dictionary dictionary =
        dictionaryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));
    dictionary.incrementViewCount();
  }
}
