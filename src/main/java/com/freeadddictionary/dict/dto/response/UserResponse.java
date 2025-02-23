package com.freeadddictionary.dict.dto.response;

import com.freeadddictionary.dict.domain.Role;
import com.freeadddictionary.dict.domain.User;

public record UserResponse(Long id, String email, String nickname, Role role) {

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getEmail(), user.getNickname(), user.getRole());
  }
}
