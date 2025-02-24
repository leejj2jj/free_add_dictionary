package com.freeadddictionary.dict.controller.view;

import com.freeadddictionary.dict.service.InquiryService;
import com.freeadddictionary.dict.service.StatisticsService;
import com.freeadddictionary.dict.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminViewController {

  private final UserService userService;
  private final InquiryService inquiryService;
  private final StatisticsService statisticsService;

  @GetMapping("/users")
  public String getUserList(@PageableDefault Pageable pageable, Model model) {
    model.addAttribute("users", userService.getAllUsers(pageable));
    return "admin/admin_users";
  }

  @GetMapping("/inquiries")
  public String getInquiries(
      @RequestParam(required = false) Boolean resolved,
      @PageableDefault Pageable pageable,
      Model model) {
    model.addAttribute("inquiries", inquiryService.getInquiries(resolved, pageable));
    model.addAttribute("resolved", resolved);
    return "admin/admin_inquiries";
  }

  @GetMapping
  public String dashboard(Model model) {
    model.addAttribute("statistics", statisticsService.getStatistics());
    return "admin/admin_dashboard";
  }
}
