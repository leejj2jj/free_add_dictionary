package com.freeadddictionary.dict.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Report extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 2000)
  private String content;

  @Column(nullable = false)
  private String authorEmail;

  @Column(nullable = false)
  private boolean resolved;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  public Report(String title, String content, String authorEmail, User user) {
    this.title = title;
    this.content = content;
    this.authorEmail = authorEmail;
    this.user = user;
    this.resolved = false;
  }

  public void resolve() {
    this.resolved = true;
  }

  public void update(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
