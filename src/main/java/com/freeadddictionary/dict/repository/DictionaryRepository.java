package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Dictionary;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

  @Query("SELECT d FROM Dictionary d WHERE d.createdAt >= :startTime ORDER BY d.createdAt DESC")
  List<Dictionary> findRecentDictionaries(LocalDateTime startTime);

  @Query("SELECT d FROM Dictionary d ORDER BY d.viewCount DESC")
  List<Dictionary> findPopularDictionaries(Pageable pageable);

  boolean existsByWord(String word);

  @Query("SELECT COUNT(d) FROM Dictionary d WHERE d.createdAt >= :startTime")
  long countTodayNewDictionaries(LocalDateTime startTime);

  Page<Dictionary> findByWordContainingIgnoreCase(String word, Pageable pageable);
}
