package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.util.LoggingUtil;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class EmailValidationService {

  @Value("classpath:blocklist/disposable-email-domains.txt")
  private Resource disposableDomainsResource;

  private final Set<String> disposableDomains = new HashSet<>();

  // 정규식: 기본적인 이메일 형식 검증
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  @PostConstruct
  public void init() {
    loadDisposableDomains();
  }

  private void loadDisposableDomains() {
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(disposableDomainsResource.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!line.isEmpty() && !line.startsWith("#")) {
          disposableDomains.add(line.toLowerCase());
        }
      }
    } catch (IOException e) {
      LoggingUtil.logStructured("error.email.blocklist.load", Map.of("message", e.getMessage()));
    }
  }

  public boolean isValidEmail(String email) {
    if (email == null || email.isEmpty()) {
      return false;
    }

    // 1. 기본 형식 검증
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      return false;
    }

    // 2. 도메인 추출
    String domain = extractDomain(email);

    // 3. 일회용 이메일 도메인 확인
    if (isDisposableDomain(domain)) {
      return false;
    }

    return true;
  }

  private String extractDomain(String email) {
    return email.substring(email.lastIndexOf('@') + 1).toLowerCase();
  }

  public boolean isDisposableDomain(String domain) {
    return disposableDomains.contains(domain.toLowerCase());
  }
}
