package com.freeadddictionary.dict.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Dictionary> dictionaries = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Inquiry> inquiries = new ArrayList<>();

  @Builder
  public User(Long id, String email, String password, String nickname, Role role) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.role = role != null ? role : Role.USER;
  }

  public void update(String nickname, String password) {
    this.nickname = nickname;
    if (password != null && !password.isEmpty()) {
      this.password = password;
    }
  }

  public void promoteToAdmin() {
    this.role = Role.ADMIN;
  }

  public boolean isAdmin() {
    return this.role == Role.ADMIN;
  }
}
