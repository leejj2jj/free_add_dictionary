package com.freeadddictionary.dict.repository;

import static com.freeadddictionary.dict.domain.QReport.report;

import com.freeadddictionary.dict.domain.Report;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ReportQueryRepository extends DynamicQueryRepository {

  public ReportQueryRepository(JPAQueryFactory queryFactory) {
    super(queryFactory);
  }

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
