package com.freeadddictionary.dict.report.api;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.dto.request.ReportAddRequest;
import com.freeadddictionary.dict.report.dto.request.ReportUpdateRequest;
import com.freeadddictionary.dict.report.dto.response.ReportResponse;
import com.freeadddictionary.dict.report.service.ReportService;
import com.freeadddictionary.dict.user.domain.User;
import com.freeadddictionary.dict.user.service.UserService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportApiController {

  private final ReportService reportService;
  private final UserService userService;

  @GetMapping("")
  public ResponseEntity<List<ReportResponse>> findAllReports() {
    List<ReportResponse> reports =
        reportService.findAll().stream().map(ReportResponse::new).toList();
    return ResponseEntity.ok().body(reports);
  }

  @PostMapping("")
  public ResponseEntity<Report> addReport(
      @Validated @RequestBody ReportAddRequest request, Principal principal) {
    String email = principal.getName();
    User user = userService.findByEmail(email);
    Report report = reportService.save(request, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(report);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReportResponse> findReport(@PathVariable long id) {
    Report report = reportService.findById(id);
    return ResponseEntity.ok().body(new ReportResponse(report));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReportResponse> updateReport(
      @PathVariable long id, @Validated @RequestBody ReportUpdateRequest request) {
    Report updatedReport = reportService.update(id, request);
    return ResponseEntity.ok().body(new ReportResponse(updatedReport));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReport(@PathVariable long id) {
    reportService.delete(id);
    return ResponseEntity.ok().build();
  }
}
