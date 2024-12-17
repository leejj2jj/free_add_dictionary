package com.freeadddictionary.dict.phrase.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "phrases")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phrase {

  @Id
  @Column(nullable = false, columnDefinition = "TEXT")
  private String phrase;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String meaning;

  @OneToMany(mappedBy = "phrase")
  private List<WordPhrase> words = new ArrayList<>();

  @Builder
  public Phrase(String phrase, String meaning) {
    this.phrase = phrase;
    this.meaning = meaning;
  }

}
