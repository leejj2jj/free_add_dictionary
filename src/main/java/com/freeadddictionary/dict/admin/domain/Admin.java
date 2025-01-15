package com.freeadddictionary.dict.admin.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
  @GeneratedValue
  @Column(name = "admin_id", updatable = false)
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

  @Builder
  public Admin(String password, String nickname) {
    this.password = password;
    this.nickname = nickname;
    this.accessDate = LocalDateTime.now();
  }

}
