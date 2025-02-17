package com.freeadddictionary.dict.report.domain;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.reportReply.domain.ReportReply;
import com.freeadddictionary.dict.shared.domain.BaseTimeEntity;
import com.freeadddictionary.dict.user.domain.DictUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "reports")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Report extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "report_id")
  private Long id;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @ManyToOne private DictUser author;

  @OneToMany(mappedBy = "report", cascade = REMOVE)
  private List<ReportReply> reportReplies = new ArrayList<>();

  @OneToMany(mappedBy = "report", cascade = REMOVE)
  private List<ReportedWord> reportedWords = new ArrayList<>();

  @Builder
  public Report(String title, String content, DictUser author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }

  public void update(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
