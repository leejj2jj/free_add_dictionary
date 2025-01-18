package com.freeadddictionary.dict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.freeadddictionary.dict.dto.AddUserRequest;
import com.freeadddictionary.dict.dto.LoginUserRequest;

@Controller
public class UserViewController {

  @GetMapping("/login")
  public String login(@ModelAttribute LoginUserRequest user) {
    return "user/login";
  }

  @GetMapping("/signup")
  public String signup(@ModelAttribute AddUserRequest user) {
    return "user/signup";
  }

}