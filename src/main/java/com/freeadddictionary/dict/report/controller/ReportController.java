package com.freeadddictionary.dict.report.controller;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.dto.response.ReportListViewResponse;
import com.freeadddictionary.dict.report.dto.response.ReportViewResponse;
import com.freeadddictionary.dict.report.service.ReportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("reports")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping("")
  public String getReportList(Model model) {
    List<ReportListViewResponse> reports =
        reportService.findAll().stream().map(ReportListViewResponse::new).toList();
    model.addAttribute("reports", reports);
    return "report/report_list";
  }

  @GetMapping("/{id}")
  public String getReport(@PathVariable Long id, Model model) {
    Report report = reportService.findById(id);
    model.addAttribute("report", new ReportViewResponse(report));
    return "report/report";
  }

  @GetMapping("/new")
  public String newReport(@RequestParam(required = false) Long id, Model model) {
    if (id == null) {
      model.addAttribute("report", new ReportViewResponse());
    } else {
      Report report = reportService.findById(id);
      model.addAttribute("report", new ReportViewResponse(report));
    }
    return "report/new_report";
  }
}
