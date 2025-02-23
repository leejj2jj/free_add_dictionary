package com.freeadddictionary.dict.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.freeadddictionary.dict.domain.Report;
import com.freeadddictionary.dict.domain.User;
import com.freeadddictionary.dict.dto.request.ReportRequest;
import com.freeadddictionary.dict.exception.ResourceNotFoundException;
import com.freeadddictionary.dict.repository.ReportRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  @Mock private ReportRepository reportRepository;

  @Mock private UserRepository userRepository;

  @InjectMocks private ReportService reportService;

  private User user;
  private Report report;
  private ReportRequest request;

  @BeforeEach
  void setUp() {
    user = User.builder().email("test@test.com").password("password").nickname("tester").build();

    report =
        Report.builder()
            .title("Test Report")
            .content("Test Content")
            .authorEmail("test@test.com")
            .user(user)
            .build();

    request = new ReportRequest();
    request.setTitle("Test Report");
    request.setContent("Test Content");
  }

  @Test
  void createReport_Success() {
    // given
    given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
    given(reportRepository.save(any())).willReturn(report);

    // when
    Report result = reportService.createReport(request, "test@test.com");

    // then
    assertThat(result.getTitle()).isEqualTo(request.getTitle());
    verify(reportRepository).save(any());
  }

  @Test
  void resolveReport_Success() {
    // given
    given(reportRepository.findById(any())).willReturn(Optional.of(report));

    // when
    Report result = reportService.resolveReport(1L);

    // then
    assertThat(result.isResolved()).isTrue();
  }

  @Test
  void resolveReport_NotFound() {
    // given
    given(reportRepository.findById(any())).willReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> reportService.resolveReport(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
