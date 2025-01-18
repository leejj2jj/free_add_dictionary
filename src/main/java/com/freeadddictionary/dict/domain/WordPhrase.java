package com.freeadddictionary.dict.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "word_phrases")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class WordPhrase {

  @Id
  @GeneratedValue
  @Column(name = "word_phrase_id")
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private Word word;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "phrase_id")
  private Phrase phrase;

  @Builder
  public WordPhrase(Word word, Phrase phrase) {
    this.word = word;
    this.phrase = phrase;
  }

}
