package com.freeadddictionary.dict.user.controller;

import com.freeadddictionary.dict.user.dto.request.LoginUserRequest;
import com.freeadddictionary.dict.user.dto.request.RegisterUserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("user", new LoginUserRequest());
    return "user/login";
  }

  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute("user", new RegisterUserRequest());
    return "user/signup";
  }
}
