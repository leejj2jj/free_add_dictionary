package com.freeadddictionary.dict.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.freeadddictionary.dict.domain.Inquiry;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.InquiryRequest;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.InquiryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

  @Mock private InquiryRepository inquiryRepository;

  @Mock private UserRepository userRepository;

  @InjectMocks private InquiryService inquiryService;

  private User user;
  private Inquiry inquiry;
  private InquiryRequest request;

  @BeforeEach
  void setUp() {
    user = User.builder().email("test@test.com").password("password").nickname("tester").build();

    inquiry =
        Inquiry.builder()
            .title("Test Inquiry")
            .content("Test Content")
            .authorEmail("test@test.com")
            .user(user)
            .build();

    request = new InquiryRequest();
    request.setTitle("Test Inquiry");
    request.setContent("Test Content");
  }

  @Test
  void createInquiry_Success() {
    // given
    given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
    given(inquiryRepository.save(any())).willReturn(inquiry);

    // when
    Inquiry result = inquiryService.createInquiry(request, "test@test.com");

    // then
    assertThat(result.getTitle()).isEqualTo(request.getTitle());
    verify(inquiryRepository).save(any());
  }

  @Test
  void resolveInquiry_Success() {
    // given
    given(inquiryRepository.findById(any())).willReturn(Optional.of(inquiry));

    // when
    Inquiry result = inquiryService.resolveInquiry(1L);

    // then
    assertThat(result.isResolved()).isTrue();
  }

  @Test
  void resolveInquiry_NotFound() {
    // given
    given(inquiryRepository.findById(any())).willReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> inquiryService.resolveInquiry(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
