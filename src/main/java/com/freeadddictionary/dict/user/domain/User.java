package com.freeadddictionary.dict.user.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.freeadddictionary.dict.address.domain.Address;
import com.freeadddictionary.dict.bookmark.domain.Bookmark;
import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.word.domain.Word;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(length = 30)
  private String phone;

  // @ColumnDefault("1")
  // @Column(columnDefinition = "TINYINT(1)")
  private boolean receivingEmail;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime registerDate;

  @LastModifiedDate
  private LocalDateTime modifyDate;

  // @Column(nullable = false)
  private LocalDateTime accessDate;

  @ManyToOne
  @JoinColumn(name = "postcode")
  private Address address;

  @OneToMany(mappedBy = "user")
  private List<Word> words = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Bookmark> bookmarks = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Report> reports = new ArrayList<>();

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
