package com.freeadddictionary.dict.word.domain;

import com.freeadddictionary.dict.bookmark.domain.Bookmark;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "bookmarked_words")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkedWord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "word_id")
  private Word word;

  @ManyToOne
  @JoinColumn(name = "bookmark_id")
  private Bookmark bookmark;

  public BookmarkedWord(Word word, Bookmark bookmark) {
    this.word = word;
    this.bookmark = bookmark;
  }
}
