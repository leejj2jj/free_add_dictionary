package com.freeadddictionary.dict.config.jwt.service;

import com.freeadddictionary.dict.config.jwt.JwtProperties;
import com.freeadddictionary.dict.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenProvider {

  private final JwtProperties jwtProperties;

  private Key getSigningKey() {
    return jwtProperties.getSigningKey();
  }

  public String generateToken(User user, Duration expiredAt) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expiredAt.toMillis());

    return makeToken(expiry, user);
  }

  private String makeToken(Date expiry, User user) {
    Date now = new Date();

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer(jwtProperties.getIssuer())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .setSubject(user.getEmail())
        .claim("id", user.getId())
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      System.err.println("Invalid JWT: " + e.getMessage());
      return false;
    }
  }

  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);
    Set<SimpleGrantedAuthority> authorities =
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

    return new UsernamePasswordAuthenticationToken(
        new org.springframework.security.core.userdetails.User(
            claims.getSubject(), "", authorities),
        token,
        authorities);
  }

  public Long getUserId(String token) {
    Claims claims = getClaims(token);
    return claims.get("id", Long.class);
  }

  private Claims getClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to parse JWT token: " + e.getMessage(), e);
    }
  }
}
