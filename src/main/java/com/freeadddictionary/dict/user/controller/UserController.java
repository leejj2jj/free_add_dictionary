package com.freeadddictionary.dict.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.freeadddictionary.dict.user.dto.request.AddUserRequest;
import com.freeadddictionary.dict.user.dto.request.LoginUserRequest;

@Controller
public class UserController {

  @GetMapping("/login")
  public String login(@ModelAttribute LoginUserRequest user) {
    return "user/login";
  }

  @GetMapping("/signup")
  public String signup(@ModelAttribute AddUserRequest user) {
    return "user/signup";
  }

}