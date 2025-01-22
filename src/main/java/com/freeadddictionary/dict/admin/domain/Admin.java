package com.freeadddictionary.dict.admin.domain;

import static lombok.AccessLevel.PROTECTED;

import com.freeadddictionary.dict.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "admins")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Admin extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "admin_id")
  private Long id;

  private String password;

  private String nickname;

  @Builder
  public Admin(String password, String nickname) {
    this.password = password;
    this.nickname = nickname;
  }
}
