package com.freeadddictionary.dict.member.api;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.freeadddictionary.dict.member.dto.request.AddMemberRequest;
import com.freeadddictionary.dict.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  @PostMapping("/member")
  public String signup(@Validated AddMemberRequest request) {
    memberService.save(request);
    return "redirect:/login";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response,
        SecurityContextHolder.getContext().getAuthentication());
    return "redirect:/";
  }

}
