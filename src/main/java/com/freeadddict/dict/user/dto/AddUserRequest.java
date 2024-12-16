package com.freeadddict.dict.user.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
  private String email;
  private String password;
  private String nickname;
  private String name;
  private String phone;
  private boolean receivingEmail;
  private LocalDateTime registerDate = LocalDateTime.now();
}
