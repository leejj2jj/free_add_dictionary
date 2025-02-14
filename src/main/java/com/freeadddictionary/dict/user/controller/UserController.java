package com.freeadddictionary.dict.user.controller;

import com.freeadddictionary.dict.user.dto.request.UserLoginRequest;
import com.freeadddictionary.dict.user.dto.request.UserRegisterRequest;
import com.freeadddictionary.dict.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("user", new UserLoginRequest());
    return "user/login";
  }

  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute("user", new UserRegisterRequest());
    return "user/signup";
  }

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
