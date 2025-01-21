package com.freeadddictionary.dict.report.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Report {

  @Id
  @GeneratedValue
  @Column(name = "report_id")
  private Long id;

  private String title;

  private String content;

  @CreatedDate
  private LocalDateTime writeDate;

  @LastModifiedDate
  private LocalDateTime modifyDate;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToOne(mappedBy = "report", fetch = LAZY)
  private ReportReply reportReply;

  @OneToMany(mappedBy = "report")
  private List<ReportedWord> reportedWords = new ArrayList<>();

  @Builder
  private Report(String title, String content, User user) {
    this.title = title;
    this.content = content;
    this.user = user;
    this.writeDate = LocalDateTime.now();
  }

  public void update(String title, String content) {
    this.title = title;
    this.content = content;
    this.modifyDate = LocalDateTime.now();
  }
}