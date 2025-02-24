package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.domain.Inquiry;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.InquiryRequest;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.InquiryQueryRepository;
import com.freeadddictionary.dict.repository.InquiryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

  private final InquiryRepository inquiryRepository;
  private final InquiryQueryRepository inquiryQueryRepository;
  private final UserRepository userRepository;

  public Page<Inquiry> getInquiries(Boolean resolved, Pageable pageable) {
    return inquiryQueryRepository.findByResolved(resolved, pageable);
  }

  public Page<Inquiry> getUserInquiries(String email, Pageable pageable) {
    return inquiryRepository.findByAuthorEmail(email, pageable);
  }

  @Transactional
  public Inquiry createInquiry(InquiryRequest request, String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

    Inquiry inquiry =
        Inquiry.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .authorEmail(email)
            .user(user)
            .build();

    return inquiryRepository.save(inquiry);
  }

  @Transactional
  public Inquiry resolveInquiry(Long id) {
    Inquiry inquiry =
        inquiryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inquiry", "id", id));
    inquiry.resolve();
    return inquiry;
  }

  @Transactional
  public void deleteInquiry(Long id) {
    if (!inquiryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Inquiry", "id", id);
    }
    inquiryRepository.deleteById(id);
  }

  public Inquiry getInquiry(Long id) {
    return inquiryRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Inquiry", "id", id));
  }
}
