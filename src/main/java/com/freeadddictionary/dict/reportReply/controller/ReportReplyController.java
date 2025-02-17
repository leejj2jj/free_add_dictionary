package com.freeadddictionary.dict.reportReply.controller;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.service.ReportService;
import com.freeadddictionary.dict.reportReply.domain.ReportReply;
import com.freeadddictionary.dict.reportReply.dto.ReportReplyAddRequest;
import com.freeadddictionary.dict.reportReply.dto.ReportReplyUpdateRequest;
import com.freeadddictionary.dict.reportReply.service.ReportReplyService;
import com.freeadddictionary.dict.user.domain.DictUser;
import com.freeadddictionary.dict.user.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/report/reply")
@RequiredArgsConstructor
public class ReportReplyController {

  private final ReportService reportService;
  private final ReportReplyService reportReplyService;
  private final UserService userService;

  @PostMapping("/create/{id}")
  @PreAuthorize("isAuthenticated()")
  public String createReportReply(
      Model model,
      @PathVariable Long id,
      @Valid ReportReplyAddRequest request,
      BindingResult bindingResult,
      Principal principal) {
    Report report = reportService.getReport(id);
    DictUser dictUser = userService.getUserByEmail(principal.getName());
    if (bindingResult.hasErrors()) {
      model.addAttribute("report", report);
      return "report_detail";
    }
    ReportReply reportReply = reportReplyService.create(report, request, dictUser);
    return String.format(
        "redirect:/report/detail/%s#reply_%s",
        reportReply.getReport().getId(), reportReply.getId());
  }

  @GetMapping("/modify/{id}")
  @PreAuthorize("isAuthenticated()")
  public String modifyReportReply(
      @Valid ReportReplyUpdateRequest request,
      BindingResult bindingResult,
      @PathVariable Long id,
      Principal principal) {
    if (bindingResult.hasErrors()) {
      return "report_detail";
    }
    ReportReply reply = reportReplyService.getReportReply(id);
    if (!reply.getAuthor().getEmail().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
    }
    reportReplyService.modify(id, request);
    return String.format(
        "redirect:/report/detail/%s#reportReply_%s", reply.getReport().getId(), reply.getId());
  }

  @GetMapping("/delete/{id}")
  @PreAuthorize("isAuthenticated()")
  public String deleteReportReply(Principal principal, @PathVariable Long id) {
    ReportReply reply = reportReplyService.getReportReply(id);
    if (!reply.getAuthor().getEmail().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
    }
    reportReplyService.delete(reply);
    return String.format("redierct:/report/detail/%s", reply.getReport().getId());
  }
}
