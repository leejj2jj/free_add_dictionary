package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Dictionary;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/** Dictionary 엔티티에 대한 사용자 정의 쿼리 인터페이스 */
@Validated
public interface DictionaryRepositoryCustom {

  /**
   * 키워드로 단어 혹은 의미를 검색한다. 대소문자를 구분하지 않으며, 키워드가 null 혹은 빈 문자열인 경우 모든 레코드를 반환한다.
   *
   * @param keyword 검색할 키워드 (null 가능)
   * @param pageable 페이징 정보 (null 불가)
   * @return 검색 조건에 맞는 사전 정보로 만들어진 페이지
   * @throws IllegalArgumentException pageable이 null인 경우
   */
  Page<Dictionary> searchByKeyword(@Nullable String keyword, @NonNull Pageable pageable);

  Optional<Dictionary> findByIdWithUser(Long id);
}
