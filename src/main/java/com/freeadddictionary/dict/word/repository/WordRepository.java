package com.freeadddictionary.dict.word.repository;

import com.freeadddictionary.dict.word.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {}
