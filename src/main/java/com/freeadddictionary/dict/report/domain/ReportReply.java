package com.freeadddictionary.dict.report.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.shared.domain.BaseEntity;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "report_replies")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class ReportReply extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "report_reply_id")
  private Long id;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "report_id")
  private Report report;

  @Builder
  public ReportReply(String title, String content, Report report) {
    this.title = title;
    this.content = content;
    this.report = report;
  }
}
