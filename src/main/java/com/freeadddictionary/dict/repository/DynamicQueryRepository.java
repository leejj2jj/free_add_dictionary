package com.freeadddictionary.dict.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class DynamicQueryRepository {

  protected final JPAQueryFactory queryFactory;

  public DynamicQueryRepository(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  protected <T> Page<T> applyPagination(
      JPAQuery<T> contentQuery, JPAQuery<Long> countQuery, Pageable pageable) {
    long total = countQuery.fetchOne() != null ? countQuery.fetchOne() : 0L;
    List<T> content =
        contentQuery.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
    return new PageImpl<>(content, pageable, total);
  }

  protected BooleanExpression containsIgnoreCase(StringPath field, String searchTerm) {
    return StringUtils.hasText(searchTerm)
        ? field.lower().contains(searchTerm.toLowerCase())
        : null;
  }
}
