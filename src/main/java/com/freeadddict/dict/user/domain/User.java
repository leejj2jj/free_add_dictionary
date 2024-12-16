package com.freeadddict.dict.user.domain;

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

import com.freeadddict.dict.address.domain.Address;
import com.freeadddict.dict.bookmark.domain.Bookmark;
import com.freeadddict.dict.report.domain.Report;
import com.freeadddict.dict.word.domain.Word;

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
  @Column(name = "id", updatable = false)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, unique = true, length = 15)
  private String nickname;

  @Column(length = 15)
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
  public User(String email, String password, String nickname, String name, String phone, boolean receivingEmail,
      LocalDateTime registerDate, String auth) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.name = name;
    this.phone = phone;
    this.receivingEmail = receivingEmail;
    this.registerDate = LocalDateTime.now();
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
