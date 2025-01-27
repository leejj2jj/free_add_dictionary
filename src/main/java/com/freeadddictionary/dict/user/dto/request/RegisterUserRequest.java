package com.freeadddictionary.dict.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterUserRequest {

  @NotBlank @Email private String email;

  @NotBlank
  @Size(min = 10, max = 60)
  private String password;

  @NotBlank
  @Size(max = 100)
  private String name;

  @NotBlank
  @Size(max = 30)
  private String phone;

  @NotNull private boolean receivingEmail;
}
