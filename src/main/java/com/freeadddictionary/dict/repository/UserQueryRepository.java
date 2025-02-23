package com.freeadddictionary.dict.repository;

import static com.freeadddictionary.dict.domain.QUser.user;

import com.freeadddictionary.dict.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class UserQueryRepository extends DynamicQueryRepository {

  public UserQueryRepository(JPAQueryFactory queryFactory) {
    super(queryFactory);
  }

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
