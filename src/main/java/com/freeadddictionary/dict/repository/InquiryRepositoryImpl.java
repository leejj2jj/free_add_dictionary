package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Inquiry;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class InquiryRepositoryImpl extends AbstractQuerydslRepository
    implements InquiryRepositoryCustom {

  public InquiryRepositoryImpl(JPAQueryFactory queryFactory) {
    super(queryFactory);
  }

  @Override
  public Page<Inquiry> searchInquiries(
      String keyword, List<InquiryStatus> statuses, Long userId, Pageable pageable) {
    BooleanBuilder predicate = new BooleanBuilder();

    // 키워드 검색 (제목 또는 내용)
    if (StringUtils.hasText(keyword)) {
      predicate.andAny(
          containsIgnoreCase(inquiry.title, keyword), containsIgnoreCase(inquiry.content, keyword));
    }

    // 상태 필터링
    if (statuses != null && !statuses.isEmpty()) {
      predicate.and(inquiry.status.in(statuses));
    }

    // 특정 사용자의 문의만 조회
    if (userId != null) {
      predicate.and(inquiry.user.id.eq(userId));
    }

    // 컨텐츠 쿼리 - N+1 문제 방지
    JPAQuery<Inquiry> contentQuery =
        queryFactory
            .selectFrom(inquiry)
            .leftJoin(inquiry.user)
            .fetchJoin()
            .where(predicate)
            .orderBy(getOrderSpecifiers(pageable, Inquiry.class));

    // 카운트 쿼리
    JPAQuery<Long> countQuery = queryFactory.select(inquiry.count()).from(inquiry).where(predicate);

    return getPage(contentQuery, countQuery, pageable);
  }
}
