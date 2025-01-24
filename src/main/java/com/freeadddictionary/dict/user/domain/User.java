package com.freeadddictionary.dict.user.domain;

import static jakarta.persistence.FetchType.LAZY;
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
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class User extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "user_id", updatable = false)
  private Long id;

  private String email;

  private String password;

  private String name;

  private String phone;

  private boolean receivingEmail;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "postcode_id")
  private Address address;

  @Builder
  public User(
      String email,
      String password,
      String name,
      String phone,
      boolean receivingEmail,
      String auth) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.phone = phone;
    this.receivingEmail = receivingEmail;
  }
}
