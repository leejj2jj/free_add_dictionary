package com.freeadddict.dict.phrase.domain;

import com.freeadddict.dict.word.domain.Word;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "word_phrases")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WordPhrase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn
  private Word word;

  @ManyToOne
  @JoinColumn(name = "phrase_id")
  private Phrase phrase;

  @Builder
  public WordPhrase(Word word, Phrase phrase) {
    this.word = word;
    this.phrase = phrase;
  }

}
