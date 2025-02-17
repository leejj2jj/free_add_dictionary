package com.freeadddictionary.dict.report.controller;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.dto.request.ReportAddRequest;
import com.freeadddictionary.dict.report.dto.request.ReportUpdateRequest;
import com.freeadddictionary.dict.report.dto.response.ReportViewResponse;
import com.freeadddictionary.dict.report.service.ReportService;
import com.freeadddictionary.dict.user.domain.DictUser;
import com.freeadddictionary.dict.user.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;
  private final UserService userService;

  @GetMapping("/list")
  public String list(
      Model model,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "") String kw) {
    Page<Report> paging = reportService.getList(page, kw);
    model.addAttribute("paging", paging);
    model.addAttribute("kw", kw);
    return "report/report_list";
  }

  @GetMapping("/detail/{id}")
  public String detail(Model model, @PathVariable Long id, ReportViewResponse response) {
    Report report = reportService.getReport(id);
    model.addAttribute("report", report);
    return "report/report_detail";
  }

  @GetMapping("/create")
  @PreAuthorize("isAuthenticated()")
  public String reportCreate(ReportAddRequest request) {
    return "report/new_report";
  }

  @PostMapping("/create")
  public String reportCreate(
      @Valid ReportAddRequest request, BindingResult bindingResult, Principal principal) {
    if (bindingResult.hasErrors()) {
      return "word/new_report";
    }
    DictUser dictUser = userService.getUserByEmail(principal.getName());
    reportService.create(request, dictUser);
    return "redirect:/word/list";
  }

  @GetMapping("/modify/{id}")
  @PreAuthorize("isAuthenticated()")
  public String reportModify(
      @Valid ReportUpdateRequest request,
      BindingResult bindingResult,
      Principal principal,
      @PathVariable Long id) {
    if (bindingResult.hasErrors()) {
      return "new_report";
    }
    Report report = reportService.getReport(id);
    if (!report.getAuthor().getEmail().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
    }
    reportService.modify(report, request);
    return String.format("redirect:/question/detail/%s", id);
  }

  @GetMapping("/delete/{id}")
  @PreAuthorize("isAuthenticated()")
  public String reportDelete(Principal principal, @PathVariable Long id) {
    Report report = reportService.getReport(id);
    if (!report.getAuthor().getEmail().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
    }
    reportService.delete(report);
    return "redierct:/";
  }
}
