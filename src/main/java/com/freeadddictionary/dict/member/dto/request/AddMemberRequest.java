package com.freeadddictionary.dict.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {

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

  @PastOrPresent private LocalDateTime registerDate = LocalDateTime.now();
}