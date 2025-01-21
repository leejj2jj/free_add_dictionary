package com.freeadddictionary.dict.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginUserRequest {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 10, max = 60)
  private String password;

}
