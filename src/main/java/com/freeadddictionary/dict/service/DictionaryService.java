package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.annotation.LogExecutionTime;
import com.freeadddictionary.dict.domain.Dictionary;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.DictionaryRequest;
import com.freeadddictionary.dict.exception.DuplicateResourceException;
import com.freeadddictionary.dict.exception.ForbiddenException;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.DictionaryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import com.freeadddictionary.dict.util.LoggingUtil;
import com.freeadddictionary.dict.util.SecurityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DictionaryService {

  private final DictionaryRepository dictionaryRepository;
  private final UserRepository userRepository;

  @LogExecutionTime
  public Page<Dictionary> searchDictionaries(String keyword, Pageable pageable) {
    return dictionaryRepository.searchByKeyword(keyword, pageable);
  }

  public Dictionary getDictionary(Long id) {
    return dictionaryRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));
  }

  @LogExecutionTime
  @Transactional
  public Dictionary createDictionary(DictionaryRequest request, String email) {
    log.debug(
        "Creating dictionary with word: {}, language: {}, by user: {}",
        request.getWord(),
        request.getLanguage(),
        email);

    // 중복 단어 체크
    if (dictionaryRepository.existsByWord(request.getWord())) {
      log.warn("Duplicate dictionary word attempted: {}, by user: {}", request.getWord(), email);
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

    Dictionary saved = dictionaryRepository.save(dictionary);

    // 구조화된 로그
    Map<String, Object> logData = new HashMap<>();
    logData.put("id", saved.getId());
    logData.put("word", saved.getWord());
    logData.put("language", saved.getLanguage());
    logData.put("partOfSpeech", saved.getPartOfSpeech());
    logData.put("meaningLength", saved.getMeaning().length());
    logData.put("createdBy", email);
    LoggingUtil.logStructured("dictionary.created", logData);

    // 감사 로그 (보안/변경 추적 목적)
    LoggingUtil.logAudit(
        "CREATE",
        "Dictionary",
        saved.getId(),
        email,
        Map.of("word", saved.getWord(), "language", saved.getLanguage()));

    log.info("Dictionary created successfully: id={}, word={}", saved.getId(), saved.getWord());
    return saved;
  }

  @Transactional
  public Dictionary updateDictionary(Long id, DictionaryRequest request, String email) {
    log.debug("Updating dictionary id: {}, word: {}, by user: {}", id, request.getWord(), email);

    Dictionary dictionary = getDictionary(id);

    // 수정 전 기존 데이터 저장 (변경 추적용)
    String oldWord = dictionary.getWord();
    String oldLanguage = dictionary.getLanguage();
    String oldMeaning = dictionary.getMeaning();

    // 관리자이거나 작성자인 경우에만 수정 가능
    if (!SecurityUtil.isAdmin() && !dictionary.getUser().getEmail().equals(email)) {
      log.warn("Unauthorized update attempt on dictionary id: {} by user: {}", id, email);
      throw new ForbiddenException("수정 권한이 없습니다.");
    }

    dictionary.update(
        request.getWord(),
        request.getLanguage(),
        request.getPartOfSpeech(),
        request.getPronunciation(),
        request.getMeaning(),
        request.getExampleSentence());

    // 구조화된 로그
    Map<String, Object> changes = new HashMap<>();
    if (!oldWord.equals(request.getWord())) {
      changes.put("word", Map.of("old", oldWord, "new", request.getWord()));
    }
    if (!oldLanguage.equals(request.getLanguage())) {
      changes.put("language", Map.of("old", oldLanguage, "new", request.getLanguage()));
    }
    if (!oldMeaning.equals(request.getMeaning())) {
      changes.put("meaning", "changed");
      changes.put("meaningLengthChange", request.getMeaning().length() - oldMeaning.length());
    }

    if (!changes.isEmpty()) {
      Map<String, Object> logData = new HashMap<>();
      logData.put("id", dictionary.getId());
      logData.put("word", dictionary.getWord());
      logData.put("updatedBy", email);
      logData.put("changes", changes);
      LoggingUtil.logStructured("dictionary.updated", logData);

      // 감사 로그
      LoggingUtil.logAudit("UPDATE", "Dictionary", dictionary.getId(), email, changes);
    }

    log.info(
        "Dictionary updated successfully: id={}, word={}",
        dictionary.getId(),
        dictionary.getWord());
    return dictionary;
  }

  @Transactional
  public void deleteDictionary(Long id) {
    log.debug("Deleting dictionary id: {}", id);

    Dictionary dictionary = getDictionary(id);
    String currentUserEmail = SecurityUtil.getCurrentUserEmail();

    // 관리자이거나 작성자인 경우에만 삭제 가능
    if (!SecurityUtil.isAdmin() && !dictionary.getUser().getEmail().equals(currentUserEmail)) {
      log.warn(
          "Unauthorized delete attempt on dictionary id: {} by user: {}", id, currentUserEmail);
      throw new ForbiddenException("삭제 권한이 없습니다.");
    }

    dictionaryRepository.delete(dictionary);

    // 구조화된 로그
    Map<String, Object> logData = new HashMap<>();
    logData.put("id", dictionary.getId());
    logData.put("word", dictionary.getWord());
    logData.put("language", dictionary.getLanguage());
    logData.put("deletedBy", currentUserEmail);
    logData.put("authorId", dictionary.getUser().getId());
    logData.put("authorEmail", dictionary.getUser().getEmail());
    logData.put(
        "isAdminDelete",
        SecurityUtil.isAdmin() && !dictionary.getUser().getEmail().equals(currentUserEmail));
    LoggingUtil.logStructured("dictionary.deleted", logData);

    // 감사 로그
    LoggingUtil.logAudit(
        "DELETE",
        "Dictionary",
        dictionary.getId(),
        currentUserEmail,
        Map.of(
            "word", dictionary.getWord(),
            "authorId", dictionary.getUser().getId(),
            "isAdminAction", SecurityUtil.isAdmin()));

    log.info(
        "Dictionary deleted successfully: id={}, word={}",
        dictionary.getId(),
        dictionary.getWord());
  }

  /**
   * 최근에 추가된 사전 항목을 가져옵니다.
   *
   * @return 최근 추가된 사전 항목 목록 (최대 10개)
   */
  @Cacheable(value = "recentDictionaries")
  @Transactional(readOnly = true)
  public List<Dictionary> getRecentDictionaries() {
    return dictionaryRepository
        .findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")))
        .getContent();
  }

  /**
   * 인기 있는 사전 항목을 가져옵니다.
   *
   * @return 조회수 기준 인기 사전 항목 목록 (최대 10개)
   */
  @Cacheable(value = "popularDictionaries")
  @Transactional(readOnly = true)
  public List<Dictionary> getPopularDictionaries() {
    return dictionaryRepository
        .findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "viewCount")))
        .getContent();
  }

  /**
   * 사전 항목과 작성자 정보를 함께 조회합니다. N+1 문제를 방지하기 위해 JOIN FETCH를 사용합니다.
   *
   * @param id 조회할 사전 항목 ID
   * @return 사전 항목과 작성자 정보
   * @throws ResourceNotFoundException 사전 항목을 찾을 수 없는 경우
   */
  @Transactional(readOnly = true)
  public Dictionary getDictionaryWithUser(Long id) {
    return dictionaryRepository
        .findByIdWithUser(id)
        .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));
  }

  /**
   * 사전 항목의 조회수를 증가시킵니다.
   *
   * @param id 조회수를 증가시킬 사전 항목 ID
   */
  @Transactional
  public void incrementViewCount(Long id) {
    Dictionary dictionary = getDictionary(id);
    dictionary.incrementViewCount();
    dictionaryRepository.save(dictionary);
  }
}
