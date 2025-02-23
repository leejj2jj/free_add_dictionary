package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Dictionary;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DictionaryQueryRepository extends DynamicQueryRepository {

  public Page<Dictionary> searchDictionaries(String keyword, Pageable pageable) {
    JPAQuery<Dictionary> contentQuery =
        queryFactory
            .selectFrom(dictionary)
            .where(containsIgnoreCaseInWord(keyword).or(containsIgnoreCaseInMeaning(keyword)));

    JPAQuery<Long> countQuery =
        queryFactory
            .select(dictionary.count())
            .from(dictionary)
            .where(containsIgnoreCaseInWord(keyword).or(containsIgnoreCaseInMeaning(keyword)));

    return applyPagination(contentQuery, countQuery, pageable);
  }

  private BooleanExpression containsIgnoreCaseInWord(String keyword) {
    return containsIgnoreCase(dictionary.word, keyword);
  }

  private BooleanExpression containsIgnoreCaseInMeaning(String keyword) {
    return containsIgnoreCase(dictionary.meaning, keyword);
  }
}
