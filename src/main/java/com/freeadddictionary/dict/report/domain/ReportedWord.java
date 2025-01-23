package com.freeadddictionary.dict.report.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.shared.domain.BaseEntity;
import com.freeadddictionary.dict.word.domain.Word;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reported_words")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class ReportedWord extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "reported_word_id")
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "report_id")
  private Report report;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "word_id")
  private Word word;

  public ReportedWord(Report report, Word word) {
    this.report = report;
    this.word = word;
  }
}
