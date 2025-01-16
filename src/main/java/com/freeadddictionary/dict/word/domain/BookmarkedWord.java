package com.freeadddictionary.dict.word.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.freeadddictionary.dict.bookmark.domain.Bookmark;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmarked_words")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookmarkedWord {

  @Id
  @GeneratedValue
  @Column(name = "bookmarked_word_id")
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "word_id")
  private Word word;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "bookmark_id")
  private Bookmark bookmark;

  public BookmarkedWord(Word word, Bookmark bookmark) {
    this.word = word;
    this.bookmark = bookmark;
  }
}
