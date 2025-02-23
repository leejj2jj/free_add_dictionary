package com.freeadddictionary.dict.controller.view;

import com.freeadddictionary.dict.domain.Report;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.service.ReportService;
import com.freeadddictionary.dict.service.StatisticsService;
import com.freeadddictionary.dict.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

  private final UserService userService;
  private final ReportService reportService;
  private final StatisticsService statisticsService;

  @GetMapping
  public String dashboard(Model model) {
    model.addAttribute("statistics", statisticsService.getStatistics());
    return "admin/admin_dashboard";
  }

  @GetMapping("/users")
  public String users(
      @RequestParam(required = false) String keyword,
      @PageableDefault Pageable pageable,
      Model model) {
    Page<User> users = userService.searchUsers(keyword, pageable);
    model.addAttribute("users", users);
    return "admin/admin_users";
  }

  @GetMapping("/reports")
  public String reports(
      @RequestParam(required = false) Boolean resolved,
      @PageableDefault Pageable pageable,
      Model model) {
    Page<Report> reports = reportService.getReports(resolved, pageable);
    model.addAttribute("reports", reports);
    model.addAttribute("resolved", resolved);
    return "admin/admin_reports";
  }
}
