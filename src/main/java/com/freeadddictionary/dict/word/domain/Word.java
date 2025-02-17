package com.freeadddictionary.dict.word.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.shared.domain.BaseTimeEntity;
import com.freeadddictionary.dict.user.domain.DictUser;
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
@Table(name = "words")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Word extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "word_id")
  private Long id;

  private String name;

  private String language;

  private String partOfSpeech;

  private String pronunciation;

  @Column(columnDefinition = "TEXT")
  private String meaning;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private DictUser author;

  @Builder
  public Word(
      String name,
      String language,
      String partOfSpeech,
      String pronunciation,
      String meaning,
      DictUser author) {
    this.name = name;
    this.language = language;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
    this.author = author;
  }

  public void update(
      String name, String language, String partOfSpeech, String pronunciation, String meaning) {
    this.name = name;
    this.language = language;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
  }
}
