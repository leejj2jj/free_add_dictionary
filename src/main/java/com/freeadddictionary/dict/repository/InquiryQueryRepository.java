package com.freeadddictionary.dict.repository;

import static com.freeadddictionary.dict.domain.QInquiry.inquiry;

import com.freeadddictionary.dict.domain.Inquiry;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class InquiryQueryRepository extends DynamicQueryRepository {

  public InquiryQueryRepository(JPAQueryFactory queryFactory) {
    super(queryFactory);
  }

  public Page<Inquiry> findByResolved(Boolean resolved, Pageable pageable) {
    JPAQuery<Inquiry> contentQuery = queryFactory.selectFrom(inquiry).where(isResolved(resolved));

    JPAQuery<Long> countQuery =
        queryFactory.select(inquiry.count()).from(inquiry).where(isResolved(resolved));

    return applyPagination(contentQuery, countQuery, pageable);
  }

  private BooleanExpression isResolved(Boolean resolved) {
    return resolved != null ? inquiry.resolved.eq(resolved) : null;
  }
}
