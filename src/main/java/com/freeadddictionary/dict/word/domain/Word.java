package com.freeadddictionary.dict.word.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.freeadddictionary.dict.admin.domain.Admin;
import com.freeadddictionary.dict.user.domain.User;

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
@Table(name = "words")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Word {

  @Id
  @GeneratedValue
  @Column(name = "word_id")
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 50)
  private String language;

  @Column(nullable = false, length = 50)
  private String partOfSpeech;

  @Column(nullable = false, length = 100)
  private String pronunciation;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String meaning;

  @CreatedDate
  private LocalDateTime addDate;

  @LastModifiedDate
  private LocalDateTime modifyDate;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "admin_id")
  private Admin admin;

  @Builder
  public Word(String name, String language, String partOfSpeech, String pronunciation, String meaning) {
    this.name = name;
    this.language = language;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
    this.addDate = LocalDateTime.now();
  }

  public void update(String name, String language, String partOfSpeech, String pronunciation, String meaning) {
    this.name = name;
    this.language = language;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
    this.modifyDate = LocalDateTime.now();
  }
}
