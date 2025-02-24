package com.freeadddictionary.dict.controller.api;

import com.freeadddictionary.dict.domain.Inquiry;
import com.freeadddictionary.dict.dto.request.InquiryRequest;
import com.freeadddictionary.dict.service.InquiryService;
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
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
public class InquiryApiController {

  private final InquiryService inquiryService;

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody InquiryRequest request) {
    String email = SecurityUtil.getCurrentUserEmail();
    Inquiry inquiry = inquiryService.createInquiry(request, email);
    return ResponseEntity.created(URI.create("/inquiry/" + inquiry.getId())).build();
  }

  @PostMapping("/{id}/resolve")
  public ResponseEntity<Void> resolve(@PathVariable Long id) {
    inquiryService.resolveInquiry(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    inquiryService.deleteInquiry(id);
    return ResponseEntity.noContent().build();
  }
}
