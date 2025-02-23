package com.freeadddictionary.dict.controller.view;

import com.freeadddictionary.dict.domain.Report;
import com.freeadddictionary.dict.dto.request.ReportRequest;
import com.freeadddictionary.dict.service.ReportService;
import com.freeadddictionary.dict.util.SecurityUtil;
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
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportViewController {

  private final ReportService reportService;

  @GetMapping
  public String list(@PageableDefault Pageable pageable, Model model) {
    String email = SecurityUtil.getCurrentUserEmail();
    Page<Report> reports = reportService.getUserReports(email, pageable);
    model.addAttribute("reports", reports);
    return "report/report_list";
  }

  @GetMapping("/{id}")
  public String detail(@PathVariable Long id, Model model) {
    Report report = reportService.getReport(id);
    model.addAttribute("report", report);
    return "report/report_detail";
  }

  @GetMapping("/form")
  public String form(@RequestParam(required = false) Long id, Model model) {
    if (id != null) {
      Report report = reportService.getReport(id);
      model.addAttribute("reportRequest", new ReportRequest(report));
    } else {
      model.addAttribute("reportRequest", new ReportRequest());
      model.addAttribute("userEmail", SecurityUtil.getCurrentUserEmail());
    }
    return "report/report_form";
  }
}
