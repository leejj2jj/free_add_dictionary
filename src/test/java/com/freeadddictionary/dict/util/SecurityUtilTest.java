package com.freeadddictionary.dict.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class SecurityUtilTest {

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void getCurrentUserEmail_Success() {
    // given
    String email = "test@test.com";
    UserDetails userDetails =
        new User(
            email, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()));

    // when
    String result = SecurityUtil.getCurrentUserEmail();

    // then
    assertThat(result).isEqualTo(email);
  }

  @Test
  void getCurrentUserEmail_NotAuthenticated() {
    assertThatThrownBy(() -> SecurityUtil.getCurrentUserEmail())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("No authenticated user found");
  }

  @Test
  void isAuthenticated_True() {
    // given
    UserDetails userDetails =
        new User(
            "test@test.com",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()));

    // when & then
    assertThat(SecurityUtil.isAuthenticated()).isTrue();
  }

  @Test
  void isAdmin_True() {
    // given
    UserDetails userDetails =
        new User(
            "admin@test.com",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()));

    // when & then
    assertThat(SecurityUtil.isAdmin()).isTrue();
  }
}
