package com.freeadddictionary.dict.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String actionType; // LOGIN, LOGOUT, CREATE_POST, etc.

  private String username;

  private String ipAddress;

  private String details;

  private LocalDateTime timestamp = LocalDateTime.now();
}
