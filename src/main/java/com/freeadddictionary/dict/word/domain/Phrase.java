package com.freeadddictionary.dict.word.domain;

import static lombok.AccessLevel.PROTECTED;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phrases")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Phrase {

  @Id
  @GeneratedValue
  @Column(name = "phrase_id")
  private Long id;

  private String phrase;

  private String meaning;

  @OneToMany(mappedBy = "phrase")
  private List<WordPhrase> words = new ArrayList<>();

  @Builder
  public Phrase(String phrase, String meaning) {
    this.phrase = phrase;
    this.meaning = meaning;
  }

}
