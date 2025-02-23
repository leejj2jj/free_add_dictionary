package com.freeadddictionary.dict.repository;

import static com.freeadddictionary.dict.domain.QDictionary.dictionary;

import com.freeadddictionary.dict.domain.Dictionary;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class DictionaryRepositoryImpl implements DictionaryRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Dictionary> searchByKeyword(@Nullable String keyword, @NonNull Pageable pageable) {
    BooleanBuilder predicate = new BooleanBuilder();
    if (StringUtils.hasText(keyword)) {
      String trimmedKeyword = keyword.trim().toLowerCase();
      predicate
          .or(dictionary.word.lower().contains(trimmedKeyword))
          .or(dictionary.meaning.lower().contains(trimmedKeyword));
    }

    List<Dictionary> results =
        queryFactory
            .selectFrom(dictionary)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable))
            .fetch();

    return PageableExecutionUtils.getPage(
        results,
        pageable,
        () ->
            queryFactory.select(dictionary.count()).from(dictionary).where(predicate).fetchOne()
                    != null
                ? queryFactory
                    .select(dictionary.count())
                    .from(dictionary)
                    .where(predicate)
                    .fetchOne()
                : 0L);
  }

  private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
    if (pageable.getSort().isEmpty()) {
      return new OrderSpecifier[] {new OrderSpecifier(Order.ASC, dictionary.word)};
    }

    return pageable.getSort().stream()
        .map(
            order ->
                new OrderSpecifier(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    getOrderTarget(order.getProperty())))
        .toArray(OrderSpecifier[]::new);
  }

  private Expression<?> getOrderTarget(String property) {
    return switch (property) {
      case "word" -> dictionary.word;
      case "language" -> dictionary.language;
      case "createdAt" -> dictionary.createdAt;
      case "viewCount" -> dictionary.viewCount;
      default -> dictionary.word;
    };
  }
}
