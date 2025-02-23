package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository extends DynamicQueryRepository {

  public Page<User> searchByEmailOrNickname(String keyword, Pageable pageable) {
    JPAQuery<User> contentQuery =
        queryFactory
            .selectFrom(user)
            .where(containsIgnoreCaseInEmail(keyword).or(containsIgnoreCaseInNickname(keyword)));

    JPAQuery<Long> countQuery =
        queryFactory
            .select(user.count())
            .from(user)
            .where(containsIgnoreCaseInEmail(keyword).or(containsIgnoreCaseInNickname(keyword)));

    return applyPagination(contentQuery, countQuery, pageable);
  }

  private BooleanExpression containsIgnoreCaseInEmail(String keyword) {
    return containsIgnoreCase(user.email, keyword);
  }

  private BooleanExpression containsIgnoreCaseInNickname(String keyword) {
    return containsIgnoreCase(user.nickname, keyword);
  }
}
