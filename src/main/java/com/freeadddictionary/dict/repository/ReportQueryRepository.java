package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Report;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportQueryRepository extends DynamicQueryRepository {

  public Page<Report> findByResolved(Boolean resolved, Pageable pageable) {
    JPAQuery<Report> contentQuery = queryFactory.selectFrom(report).where(isResolved(resolved));

    JPAQuery<Long> countQuery =
        queryFactory.select(report.count()).from(report).where(isResolved(resolved));

    return applyPagination(contentQuery, countQuery, pageable);
  }

  private BooleanExpression isResolved(Boolean resolved) {
    return resolved != null ? report.resolved.eq(resolved) : null;
  }
}
