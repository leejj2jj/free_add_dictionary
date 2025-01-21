package com.freeadddictionary.dict.report.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.freeadddictionary.dict.admin.domain.Admin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report_replies")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class ReportReply {

  @Id
  @GeneratedValue
  @Column(name = "report_reply_id")
  private Long id;

  private String title;

  private String content;

  @CreatedDate
  private LocalDateTime writeDate;

  @LastModifiedDate
  private LocalDateTime modifyDate;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "admin_id")
  private Admin admin;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "report_id")
  private Report report;

  @Builder
  public ReportReply(String title, String content, Admin admin, Report report) {
    this.title = title;
    this.content = content;
    this.admin = admin;
    this.report = report;
  }

}
