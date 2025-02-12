package com.freeadddictionary.dict.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginRequest {

  @NotBlank(message = "이메일을 입력해 주세요.")
  @Email(message = "이메일 형식이 잘못되었습니다.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해 주세요.")
  @Size(min = 10, message = "비밀번호는 10자 이상이어야 합니다.")
  private String password;
}
