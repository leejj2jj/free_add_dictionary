package com.freeadddictionary.dict.word.repository;

import com.freeadddictionary.dict.word.domain.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordRepository extends JpaRepository<Word, Long> {

  @Query(
      "select "
          + "distinct w "
          + "from Word w "
          + "left outer join DictUser u on w.author = u "
          + "where "
          + "w.name like %:kw% "
          + "   or w.language like %:kw% "
          + "   or w.partOfSpeech like %:kw% "
          + "   or w.pronunciation like %:kw% "
          + "   or w.meaning like %:kw% "
          + "   or u.username like %:kw%")
  Page<Word> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
