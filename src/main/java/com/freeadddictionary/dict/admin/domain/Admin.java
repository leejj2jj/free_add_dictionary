package com.freeadddictionary.dict.admin.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.freeadddictionary.dict.reportReply.domain.ReportReply;
import com.freeadddictionary.dict.todaysWord.domain.TodaysWord;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "admins")
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @Column(nullable = false, length = 60)
  private String password;

  @Column(nullable = false, unique = true, length = 15)
  private String nickname;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime addDate;

  @LastModifiedDate
  private LocalDateTime modifyDate;

  private LocalDateTime accessDate;

  @OneToMany(mappedBy = "admin")
  private List<ReportReply> reportReplies = new ArrayList<>();

  @OneToMany(mappedBy = "admin")
  private List<TodaysWord> todaysWords = new ArrayList<>();

  @Builder
  public Admin(String password, String nickname) {
    this.password = password;
    this.nickname = nickname;
    this.accessDate = LocalDateTime.now();
  }

}
