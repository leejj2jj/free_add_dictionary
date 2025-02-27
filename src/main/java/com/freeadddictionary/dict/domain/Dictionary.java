package com.freeadddictionary.dict.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    indexes = {
      @Index(name = "idx_dictionary_word", columnList = "word"),
      @Index(name = "idx_dictionary_language", columnList = "language")
    })
@Getter
@NoArgsConstructor
public class Dictionary extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String word;

  @Column(nullable = false)
  private String language;

  @Column(nullable = false)
  private String partOfSpeech;

  private String pronunciation;

  @Column(nullable = false, length = 1000)
  private String meaning;

  @Column(length = 1000)
  private String exampleSentence;

  @Column(nullable = false)
  private Long viewCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  public Dictionary(
      Long id,
      String word,
      String language,
      String partOfSpeech,
      String pronunciation,
      String meaning,
      String exampleSentence,
      User user) {
    this.id = id;
    this.word = word;
    this.language = language;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
    this.exampleSentence = exampleSentence;
    this.user = user;
    this.viewCount = 0L;
  }

  public void update(
      String word,
      String language,
      String partOfSpeech,
      String pronunciation,
      String meaning,
      String exampleSentence) {
    this.word = word;
    this.language = language;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
    this.exampleSentence = exampleSentence;
  }

  public void incrementViewCount() {
    this.viewCount++;
  }
}
