package com.freeadddictionary.dict.word.service;

import com.freeadddictionary.dict.shared.exception.DataNotFoundException;
import com.freeadddictionary.dict.user.domain.DictUser;
import com.freeadddictionary.dict.word.domain.Word;
import com.freeadddictionary.dict.word.dto.request.WordAddRequest;
import com.freeadddictionary.dict.word.dto.request.WordUpdateRequest;
import com.freeadddictionary.dict.word.repository.WordRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WordService {

  private final WordRepository wordRepository;

  public Page<Word> getList(int page, String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createdAt"));
    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
    return wordRepository.findAllByKeyword(kw, pageable);
  }

  public Word getWord(Long id) {
    Optional<Word> word = wordRepository.findById(id);
    if (word.isPresent()) {
      return word.get();
    } else {
      throw new DataNotFoundException("Word not found with id: " + id);
    }
  }

  @Transactional
  public void create(WordAddRequest request, DictUser user) {
    Word word =
        Word.builder()
            .name(request.getName())
            .language(request.getLanguage())
            .partOfSpeech(request.getPartOfSpeech())
            .pronunciation(request.getMeaning())
            .meaning(request.getMeaning())
            .author(user)
            .build();
    wordRepository.save(word);
  }

  @Transactional
  public void modify(Long id, WordUpdateRequest request) {
    Word word =
        wordRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Word not found with: " + id));
    word.update(
        request.getName(),
        request.getLanguage(),
        request.getPartOfSpeech(),
        request.getPronunciation(),
        request.getMeaning());
    wordRepository.save(word);
  }

  @Transactional
  public void delete(Long id) {
    wordRepository.deleteById(id);
  }
}
