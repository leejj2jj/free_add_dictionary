package com.freeadddictionary.dict.word.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.shared.domain.BaseEntity;
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
public class Word extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "word_id", updatable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String language;

  @Column(nullable = false)
  private String partOfSpeech;

  @Column(nullable = false)
  private String pronunciation;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String meaning;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private DictUser user;

  @Builder
  public Word(
      String name,
      String language,
      String partOfSpeech,
      String pronunciation,
      String meaning,
      DictUser user) {
    this.name = name;
    this.language = language;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
    this.user = user;
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
