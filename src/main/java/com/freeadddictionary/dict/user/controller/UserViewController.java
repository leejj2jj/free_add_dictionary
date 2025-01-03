package com.freeadddictionary.dict.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.freeadddictionary.dict.user.dto.AddUserRequest;
import com.freeadddictionary.dict.user.dto.LoginUserRequest;

@Controller
public class UserViewController {

  @GetMapping("/login")
  public String login(@ModelAttribute("user") LoginUserRequest user) {
    return "user/login";
  }

  @GetMapping("/signup")
  public String signup(@ModelAttribute("user") AddUserRequest user) {
    return "user/signup";
  }

}