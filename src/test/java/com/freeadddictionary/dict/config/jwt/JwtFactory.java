package com.freeadddictionary.dict.config.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtFactory {
  private String subject = "test@email.com";
  private Date issuedAt = new Date();
  private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
  private Map<String, Object> claims = Collections.emptyMap();

  @Builder
  public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
    this.subject = subject != null ? subject : this.subject;
    this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
    this.expiration = expiration != null ? expiration : this.expiration;
    this.claims = claims != null ? claims : this.claims;
  }

  public static JwtFactory withDefaultValues() {
    Date now = new Date();
    return JwtFactory.builder()
        .issuedAt(now)
        .expiration(new Date(now.getTime() + Duration.ofDays(14).toMillis()))
        .build();
  }

  public String createToken(JwtProperties jwtProperties) {
    return Jwts.builder()
        .setSubject(subject)
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer(jwtProperties.getIssuer())
        .setIssuedAt(issuedAt)
        .setExpiration(expiration)
        .addClaims(claims)
        .signWith(
            Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()), SignatureAlgorithm.HS256)
        .compact();
  }
}
