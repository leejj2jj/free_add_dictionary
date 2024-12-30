package com.freeadddictionary.dict.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.freeadddictionary.dict.user.domain.User;

@Controller
public class UserViewController {

  @ModelAttribute("user")
  public User user() {
    return new User();
  }

  @GetMapping("/login")
  public String login(Model model) {
    return "login";
  }

  @GetMapping("/signup")
  public String signup(Model model) {
    return "signup";
  }

}
