package com.freeadddictionary.dict.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public abstract class AbstractQuerydslRepository {

  protected final JPAQueryFactory queryFactory;

  public AbstractQuerydslRepository(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  /**
   * 페이징 처리를 적용하여 결과를 반환합니다.
   *
   * @param contentQuery 컨텐츠 조회 쿼리
   * @param countQuery 카운트 조회 쿼리
   * @param pageable 페이징 정보
   * @return 페이징 처리 된 결과
   */
  protected <T> Page<T> getPage(
      JPAQuery<T> contentQuery, JPAQuery<Long> countQuery, Pageable pageable) {
    List<T> content =
        contentQuery.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  /**
   * 페이징 처리를 적용하여 결과를 반환합니다.
   *
   * @param contentQuery 컨텐츠 조회 쿼리
   * @param totalSupplier 전체 개수 계산 Supplier
   * @param pageable 페이징 정보
   * @return 페이징 처리 된 결과
   */
  protected <T> Page<T> getPage(
      JPAQuery<T> contentQuery, Supplier<Long> totalSupplier, Pageable pageable) {
    List<T> content =
        contentQuery.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

    return new PageImpl<>(content, pageable, totalSupplier.get());
  }

  /**
   * Pageable의 정렬 정보를 QueryDSL OrderSpecifier 배열로 변환합니다.
   *
   * @param pageable 페이징 정보
   * @param entityClass 엔티티 클래스
   * @return OrderSpecifier 배열
   */
  protected OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, Class<?> entityClass) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    // 정렬 정보가 없으면 기본 정렬(ID 오름차순) 적용
    if (pageable.getSort().isEmpty()) {
      orders.add(new OrderSpecifier<>(Order.ASC, Expressions.stringPath("id")));
      return orders.toArray(new OrderSpecifier[0]);
    }

    // Pageable의 정렬 정보 변환
    pageable.getSort().stream()
        .forEach(
            order -> {
              Order direction = order.isAscending() ? Order.ASC : Order.DESC;
              String property = order.getProperty();
              Path<?> path;

              try {
                // 엔티티 클래스에서 해당 속성에 대한 경로 가져오기
                path = Expressions.path(Object.class, entityClass, property);
                orders.add(new OrderSpecifier<>(direction, path));
              } catch (Exception e) {
                // 경로를 찾을 수 없으면 단순 문자열 경로로 적용
                orders.add(new OrderSpecifier<>(direction, Expressions.stringPath(property)));
              }
            });

    return orders.toArray(new OrderSpecifier[0]);
  }

  /**
   * 문자열이 포함되었는지 확인하는 표현식을 생성합니다(대소문자 구분 없음).
   *
   * @param field 검색할 필드
   * @param searchTerm 검색어
   * @return BooleanExpression
   */
  protected BooleanExpression containsIgnoreCase(StringPath field, String searchTerm) {
    return StringUtils.hasText(searchTerm)
        ? field.lower().contains(searchTerm.toLowerCase())
        : null;
  }

  /**
   * 여러 검색 조건을 OR로 결합합니다.
   *
   * @param expressions 검색 조건들
   * @return 결합된 BooleanBuilder
   */
  protected BooleanBuilder orConditions(BooleanExpression... expressions) {
    BooleanBuilder builder = new BooleanBuilder();

    for (BooleanExpression expression : expressions) {
      if (expression != null) {
        builder.or(expression);
      }
    }

    return builder;
  }

  /**
   * 여러 검색 조건을 AND로 결합합니다.
   *
   * @param expressions 검색 조건들
   * @return 결합된 BooleanBuilder
   */
  protected BooleanBuilder andConditions(BooleanExpression... expressions) {
    BooleanBuilder builder = new BooleanBuilder();

    for (BooleanExpression expression : expressions) {
      if (expression != null) {
        builder.and(expression);
      }
    }

    return builder;
  }
}
