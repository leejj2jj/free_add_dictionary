package com.freeadddictionary.dict.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegisterRequest {

  @NotBlank(message = "이메일을 입력해 주세요.")
  @Email(message = "이메일 형식이 잘못되었습니다.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해 주세요.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[A-Za-z\\d!@#$%^&*()-+=]{10,}$",
      message = "비밀번호는 영문 대소문자, 숫자, 특수 문자를 반드시 하나씩 포함하여 10자 이상으로 지어 주세요.")
  private String password1;

  @NotBlank(message = "비밀번호 확인을 진행해 주세요.")
  private String password2;

  @NotBlank(message = "닉네임을 입력해 주세요.")
  private String name;

  @NotBlank(message = "전화번호를 입력해 주세요.")
  private String phone;

  @NotNull private boolean receivingEmail;
}
