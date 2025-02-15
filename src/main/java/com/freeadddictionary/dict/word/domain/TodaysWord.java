package com.freeadddictionary.dict.word.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.shared.domain.BaseEntity;
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
@Table(name = "todays_words")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class TodaysWord extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "todays_word_id")
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "word_id")
  private Word word;

  @Builder
  public TodaysWord(Word word) {
    this.word = word;
  }
}
