package com.freeadddictionary.dict.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class User implements UserDetails {

  @Id
  @GeneratedValue
  @Column(name = "user_id")
  private Long id;

  private String email;

  private String password;

  private String name;

  private String phone;

  private boolean receivingEmail;

  @CreatedDate
  private LocalDateTime registerDate;

  @LastModifiedDate
  private LocalDateTime modifyDate;

  private LocalDateTime accessDate;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "postcode_id")
  private Address address;

  @Builder
  public User(String email, String password, String name, String phone, boolean receivingEmail,
      LocalDateTime registerDate, String auth) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.phone = phone;
    this.receivingEmail = receivingEmail;
    this.registerDate = LocalDateTime.now();
  }

  public User update(String name) {
    this.name = name;
    return this;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("user"));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
