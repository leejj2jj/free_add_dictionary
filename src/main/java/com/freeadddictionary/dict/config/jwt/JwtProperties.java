package com.freeadddictionary.dict.config.jwt;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtProperties {

  private String issuer;
  private String secretKey;

  private Key signingKey;

  @PostConstruct
  private void initialize() {
    this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public Key getSigningKey() {
    return signingKey;
  }
}
