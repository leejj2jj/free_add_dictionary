package com.freeadddictionary.dict.word.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freeadddictionary.dict.word.domain.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

}
