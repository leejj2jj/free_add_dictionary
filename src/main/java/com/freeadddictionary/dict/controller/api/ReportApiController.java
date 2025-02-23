package com.freeadddictionary.dict.controller.api;

import com.freeadddictionary.dict.domain.Report;
import com.freeadddictionary.dict.dto.request.ReportRequest;
import com.freeadddictionary.dict.service.ReportService;
import com.freeadddictionary.dict.util.SecurityUtil;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportApiController {

  private final ReportService reportService;

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody ReportRequest request) {
    String email = SecurityUtil.getCurrentUserEmail();
    Report report = reportService.createReport(request, email);
    return ResponseEntity.created(URI.create("/report/" + report.getId())).build();
  }

  @PostMapping("/{id}/resolve")
  public ResponseEntity<Void> resolve(@PathVariable Long id) {
    reportService.resolveReport(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    reportService.deleteReport(id);
    return ResponseEntity.noContent().build();
  }
}
