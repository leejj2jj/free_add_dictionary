package com.freeadddictionary.dict.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class DynamicQueryRepository {
  private final JPAQueryFactory queryFactory;

  public <T> Page<T> applyPagination(
      JPAQuery<T> contentQuery, JPAQuery<Long> countQuery, Pageable pageable) {
    long total = countQuery.fetchOne();
    List<T> content =
        contentQuery.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

    return new PageImpl<>(content, pageable, total != null ? total : 0L);
  }

  protected BooleanExpression containsIgnoreCase(String value, String searchTerm) {
    return StringUtils.hasText(searchTerm)
        ? value.toLowerCase().contains(searchTerm.toLowerCase())
        : null;
  }
}
