package com.freeadddictionary.dict.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.freeadddictionary.dict.member.dto.request.AddMemberRequest;
import com.freeadddictionary.dict.member.dto.request.LoginMemberRequest;

@Controller
public class MemberController {

  @GetMapping("/login")
  public String login(@ModelAttribute LoginMemberRequest member) {
    return "member/login";
  }

  @GetMapping("/signup")
  public String signup(@ModelAttribute AddMemberRequest member) {
    return "member/signup";
  }

}