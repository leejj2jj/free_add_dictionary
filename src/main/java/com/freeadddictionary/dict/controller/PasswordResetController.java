package com.freeadddictionary.dict.controller;

import com.freeadddictionary.dict.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @GetMapping("/user/password-reset")
  public String showPasswordResetForm() {
    return "user/password_reset_request";
  }

  @PostMapping("/user/password-reset")
  public String processPasswordReset(
      @RequestParam("email") String email, RedirectAttributes redirectAttributes) {

    boolean result = passwordResetService.sendPasswordResetEmail(email);

    if (result) {
      redirectAttributes.addFlashAttribute("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다.");
    } else {
      redirectAttributes.addFlashAttribute("error", "이메일 전송에 실패했습니다. 이메일 주소를 확인해 주세요.");
    }
    return "redirect:/user/login";
  }

  @GetMapping("/user/password-reset-confirm")
  public String showPasswordResetConfirmForm(@RequestParam String token, Model model) {

    boolean isValid = passwordResetService.validatePasswordResetToken(token);

    if (isValid) {
      model.addAttribute("token", token);
      return "user/password_reset_confirm";
    } else {
      model.addAttribute("error", "유효하지 않거나 만료된 토큰입니다.");
      return "user/password_reset_error";
    }
  }

  @PostMapping("/user/password-reset-confirm")
  public String showPasswordResetConfirmation(
      @RequestParam String token,
      @RequestParam String password,
      RedirectAttributes redirectAttributes) {

    boolean result = passwordResetService.resetPassword(token, password);

    if (result) {
      redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다. 새 비밀번호로 로그인해 주세요.");
      return "redirect:/user/login";
    } else {
      redirectAttributes.addFlashAttribute("error", "비밀번호 변경에 실패했습니다. 다시 시도해 주세요.");
      return "redirect:/user/password_reset";
    }
  }
}
