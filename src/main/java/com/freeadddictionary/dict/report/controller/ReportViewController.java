package com.freeadddictionary.dict.report.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.dto.ReportListViewResponse;
import com.freeadddictionary.dict.report.dto.ReportViewResponse;
import com.freeadddictionary.dict.report.service.ReportService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReportViewController {

  private final ReportService reportService;

  @GetMapping("/reports")
  public String getReports(Model model) {
    List<ReportListViewResponse> reports = reportService.findAll().stream().map(ReportListViewResponse::new).toList();

    model.addAttribute("reports", reports);
    return "report/reportList";
  }

  @GetMapping("/reports/{id}")
  public String getReport(@PathVariable Long id, Model model) {
    Report report = reportService.findById(id);
    model.addAttribute("report", new ReportViewResponse(report));

    return "report/report";
  }

  @GetMapping("/report/new")
  public String newReport(@RequestParam(required = false) Long id, Model model) {
    if (id == null) {
      model.addAttribute("report", new ReportViewResponse());
    } else {
      Report report = reportService.findById(id);
      model.addAttribute("report", new ReportViewResponse(report));
    }

    return "report/newReport";
  }
}
