package com.freeadddictionary.dict.user.api;

import com.freeadddictionary.dict.user.dto.request.UserRegisterRequest;
import com.freeadddictionary.dict.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserApiController {

  private final UserService userService;

  @PostMapping("/user")
  public String signup(@Validated UserRegisterRequest request) {
    userService.save(request);
    return "redirect:/login";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler()
        .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    return "redirect:/";
  }
}
