package com.freeadddictionary.dict.user.dto;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Length(min = 10, max = 60)
  private String password;

  @NotBlank
  @Length(max = 100)
  private String name;

  @NotBlank
  @Length(max = 30)
  private String phone;

  @NotNull
  private boolean receivingEmail;

  @PastOrPresent
  private LocalDateTime registerDate = LocalDateTime.now();
}
