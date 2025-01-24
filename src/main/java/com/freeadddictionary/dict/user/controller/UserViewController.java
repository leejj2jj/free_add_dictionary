package com.freeadddictionary.dict.user.controller;

import com.freeadddictionary.dict.user.dto.request.UserAddRequest;
import com.freeadddictionary.dict.user.dto.request.UserLoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("user", new UserLoginRequest());
    return "user/login";
  }

  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute("user", new UserAddRequest());
    return "user/signup";
  }
}
