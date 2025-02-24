package com.freeadddictionary.dict.controller.view;

import com.freeadddictionary.dict.domain.Inquiry;
import com.freeadddictionary.dict.dto.request.InquiryRequest;
import com.freeadddictionary.dict.service.InquiryService;
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
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryViewController {

  private final InquiryService inquiryService;

  @GetMapping("/list")
  public String list(@PageableDefault Pageable pageable, Model model) {
    String email = SecurityUtil.getCurrentUserEmail();
    Page<Inquiry> inquiries = inquiryService.getUserInquiries(email, pageable);
    model.addAttribute("inquiries", inquiries);
    return "inquiry/inquiry_list";
  }

  @GetMapping("/detail/{id}")
  public String detail(@PathVariable Long id, Model model) {
    Inquiry inquiry = inquiryService.getInquiry(id);
    model.addAttribute("inquiry", inquiry);
    return "inquiry/inquiry_detail";
  }

  @GetMapping("/form")
  public String form(@RequestParam(required = false) Long id, Model model) {
    if (id != null) {
      Inquiry inquiry = inquiryService.getInquiry(id);
      model.addAttribute("inquiryRequest", new InquiryRequest(inquiry));
    } else {
      model.addAttribute("inquiryRequest", new InquiryRequest());
    }
    return "inquiry/inquiry_form";
  }
}
