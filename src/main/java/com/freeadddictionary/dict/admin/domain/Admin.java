package com.freeadddictionary.dict.admin.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "admins")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Admin {

  @Id
  @GeneratedValue
  @Column(name = "admin_id")
  private Long id;

  private String password;

  private String nickname;

  @CreatedDate private LocalDateTime addDate;

  @LastModifiedDate private LocalDateTime modifyDate;

  private LocalDateTime accessDate;

  @Builder
  public Admin(String password, String nickname) {
    this.password = password;
    this.nickname = nickname;
    this.accessDate = LocalDateTime.now();
  }
}
