package com.freeadddictionary.dict.bookmark.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@Table(name = "bookmarks")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Bookmark {

  @Id
  @GeneratedValue
  @Column(name = "bookmark_id")
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime makeDate;

  @LastModifiedDate
  private LocalDateTime modifyDate;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Builder
  public Bookmark(String name) {
    this.name = name;
  }

}
