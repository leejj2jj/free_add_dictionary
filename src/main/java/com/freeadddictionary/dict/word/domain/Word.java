package com.freeadddictionary.dict.word.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.freeadddictionary.dict.admin.domain.Admin;
import com.freeadddictionary.dict.phrase.domain.WordPhrase;
import com.freeadddictionary.dict.todaysWord.domain.TodaysWord;
import com.freeadddictionary.dict.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "words")
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Word {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

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

  @ManyToOne
  @JoinColumn
  private User user;

  @ManyToOne
  @JoinColumn
  private Admin admin;

  @OneToMany(mappedBy = "word")
  private List<TodaysWord> todaysWords = new ArrayList<>();

  @OneToMany(mappedBy = "word")
  private List<BookmarkedWord> bookmarks = new ArrayList<>();

  @OneToMany(mappedBy = "word")
  private List<WordPhrase> wordPhrases = new ArrayList<>();

  @OneToMany(mappedBy = "word")
  private List<ReportedWord> reports = new ArrayList<>();

  public Word(String name, String partOfSpeech, String pronunciation, String meaning) {
    this.name = name;
    this.partOfSpeech = partOfSpeech;
    this.pronunciation = pronunciation;
    this.meaning = meaning;
  }
}
