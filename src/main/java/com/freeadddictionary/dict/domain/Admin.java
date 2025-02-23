package com.freeadddictionary.dict.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "admins")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Admin extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Builder
  public Admin(Long id, String email, String password, String nickname) {
    validateFields(email, password, nickname);
    this.id = id;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
  }

  public void updateAdminInfo(String nickname, String password) {
    validateFields(this.email, password, nickname);
    this.nickname = nickname;
    this.password = password;
  }

  private void validateFields(String email, String password, String nickname) {
    if (!StringUtils.hasText(email)) {
      throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
    }
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new IllegalArgumentException("유효한 이메일 주소를 입력하세요.");
    }
    if (!StringUtils.hasText(password) || password.length() < 10) {
      throw new IllegalArgumentException("비밀번호는 최소 10자 이상이어야 합니다.");
    }
    if (!StringUtils.hasText(nickname)) {
      throw new IllegalArgumentException("닉네임은 필수 입력값입니다.");
    }
  }
}
