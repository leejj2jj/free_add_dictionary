package com.freeadddictionary.dict.word.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.freeadddictionary.dict.admin.domain.Admin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
@Getter
public class TodaysWord {

  @Id
  @GeneratedValue
  @Column(name = "todays_word_id")
  private Long id;

  @CreatedDate
  private LocalDateTime todayDate;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "word_id")
  private Word word;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "admin_id")
  private Admin admin;

  @Builder
  public TodaysWord(Word word, Admin admin) {
    this.word = word;
    this.admin = admin;
  }

}
