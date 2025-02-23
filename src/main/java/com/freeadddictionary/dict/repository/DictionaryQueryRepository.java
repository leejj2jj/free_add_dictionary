package com.freeadddictionary.dict.repository;

import static com.freeadddictionary.dict.domain.QDictionary.dictionary;

import com.freeadddictionary.dict.domain.Dictionary;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class DictionaryQueryRepository extends DynamicQueryRepository {

  public DictionaryQueryRepository(JPAQueryFactory queryFactory) {
    super(queryFactory);
  }

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
