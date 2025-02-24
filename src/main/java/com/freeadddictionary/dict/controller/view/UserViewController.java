package com.freeadddictionary.dict.controller.view;

import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.UserRequest;
import com.freeadddictionary.dict.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {

  private final UserService userService;

  @GetMapping("/login")
  public String loginForm() {
    return "user/user_login";
  }

  @GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("userRequest", new UserRequest());
    return "user/user_signup";
  }

  @GetMapping("/detail/{id}")
  public String detail(@PathVariable Long id, Model model) {
    User user = userService.getUser(id);
    model.addAttribute("user", user);
    return "user/user_detail";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    User user = userService.getUser(id);
    model.addAttribute("userRequest", new UserRequest(user));
    return "user/user_edit";
  }

  @GetMapping("/list")
  public String list(
      @RequestParam(required = false) String keyword,
      @PageableDefault Pageable pageable,
      Model model) {
    Page<User> users = userService.searchUsers(keyword, pageable);
    model.addAttribute("users", users);
    return "user/user_list";
  }
}
