package com.freeadddictionary.dict.user.controller;

import com.freeadddictionary.dict.user.dto.request.UserAddRequest;
import com.freeadddictionary.dict.user.dto.request.UserLoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {

  @GetMapping("/login")
  public String login(@ModelAttribute UserLoginRequest user) {
    return "user/login";
  }

  @GetMapping("/signup")
  public String signup(@ModelAttribute UserAddRequest user) {
    return "user/signup";
  }
}
