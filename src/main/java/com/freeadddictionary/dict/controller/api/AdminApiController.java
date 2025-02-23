package com.freeadddictionary.dict.controller.api;

import com.freeadddictionary.dict.dto.request.AdminRequest;
import com.freeadddictionary.dict.dto.response.AdminResponse;
import com.freeadddictionary.dict.service.AdminService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApiController {
  private final AdminService adminService;

  @PostMapping
  public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminRequest request) {
    AdminResponse response = adminService.createAdmin(request);
    return ResponseEntity.created(URI.create("/api/admin/" + response.id())).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AdminResponse> getAdmin(@PathVariable Long id) {
    return ResponseEntity.ok(adminService.getAdminById(id));
  }

  @GetMapping
  public ResponseEntity<List<AdminResponse>> getAllAdmins() {
    return ResponseEntity.ok(adminService.getAllAdmins());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateAdmin(
      @PathVariable Long id, @Valid @RequestBody AdminRequest request) {
    adminService.updateAdmin(id, request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
    adminService.deleteAdmin(id);
    return ResponseEntity.noContent().build();
  }
}
