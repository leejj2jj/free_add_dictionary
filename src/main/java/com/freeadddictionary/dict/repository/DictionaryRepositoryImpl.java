package com.freeadddictionary.dict.repository;

import static com.freeadddictionary.dict.domain.QDictionary.dictionary;

import com.freeadddictionary.dict.domain.Dictionary;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class DictionaryRepositoryImpl extends AbstractQuerydslRepository
    implements DictionaryRepositoryCustom {

  public DictionaryRepositoryImpl(JPAQueryFactory queryFactory) {
    super(queryFactory);
  }

  @Override
  public Page<Dictionary> searchByKeyword(@Nullable String keyword, @NonNull Pageable pageable) {
    BooleanBuilder predicate = new BooleanBuilder();

    if (StringUtils.hasText(keyword)) {
      String trimmedKeyword = keyword.trim().toLowerCase();
      predicate =
          orConditions(
              containsIgnoreCase(dictionary.word, trimmedKeyword),
              containsIgnoreCase(dictionary.meaning, trimmedKeyword));
    }

    // 컨텐츠 조회 쿼리
    JPAQuery<Dictionary> contentQuery =
        queryFactory
            .selectFrom(dictionary)
            .leftJoin(dictionary.user)
            .fetchJoin() // N+1 문제 해결
            .where(predicate)
            .orderBy(getOrderSpecifiers(pageable, Dictionary.class));

    // 카운트 쿼리
    JPAQuery<Long> countQuery =
        queryFactory.select(dictionary.count()).from(dictionary).where(predicate);

    // 공통 페이징 처리 메서드 사용
    return getPage(contentQuery, countQuery, pageable);
  }

  @Override
  public Optional<Dictionary> findByIdWithUser(Long id) {
    Dictionary result =
        queryFactory
            .selectFrom(dictionary)
            .leftJoin(dictionary.user)
            .fetchJoin() // 사용자 정보 함께 로드
            .where(dictionary.id.eq(id))
            .fetchOne();

    return Optional.ofNullable(result);
  }
}
