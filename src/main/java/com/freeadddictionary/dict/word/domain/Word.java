package com.freeadddictionary.dict.word.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.BaseEntity;
import com.freeadddictionary.dict.admin.domain.Admin;
import com.freeadddictionary.dict.member.domain.Member;
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
  @GeneratedValue
  @Column(name = "word_id")
  private Long id;

  private String name;

  private String language;

  private String partOfSpeech;

  private String pronunciation;

  private String meaning;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "admin_id")
  private Admin admin;

  @Builder
  public Word(
      String name, String language, String partOfSpeech, String pronunciation, String meaning) {
    this.name = name;
    this.language = language;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
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
