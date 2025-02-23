package com.freeadddictionary.dict.dto.request;

import com.freeadddictionary.dict.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
  private Long id;

  @NotBlank(message = "이메일을 입력해주세요")
  @Email(message = "올바른 이메일 형식이 아닙니다")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요")
  @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
  private String password;

  @NotBlank(message = "닉네임을 입력해주세요")
  @Size(max = 50, message = "닉네임은 50자를 초과할 수 없습니다")
  private String nickname;

  public UserRequest(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.nickname = user.getNickname();
  }
}
