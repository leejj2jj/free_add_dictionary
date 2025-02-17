package com.freeadddictionary.dict.user.controller;

import com.freeadddictionary.dict.user.dto.request.UserRegisterRequest;
import com.freeadddictionary.dict.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @GetMapping("/login")
  public String login() {
    return "user/login";
  }

  @GetMapping("/signup")
  public String signup(UserRegisterRequest request) {
    return "user/signup";
  }

  @PostMapping("/signup")
  public String signup(@Valid UserRegisterRequest request, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "signup";
    }
    if (!request.getPassword1().equals(request.getPassword2())) {
      bindingResult.rejectValue("password2", "passwordIncorrect", "2개의 비밀번호가 일치하지 않습니다.");
      return "signup";
    }
    try {
      userService.create(request);
    } catch (DataIntegrityViolationException e) {
      e.printStackTrace();
      bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
      return "signup";
    } catch (Exception e) {
      e.printStackTrace();
      bindingResult.reject("signupFailed", e.getMessage());
      return "signup";
    }
    return "redirect:/";
  }
}
