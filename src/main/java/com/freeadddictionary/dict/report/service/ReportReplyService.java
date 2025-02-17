package com.freeadddictionary.dict.report.service;

import com.freeadddictionary.dict.report.domain.Report;
import com.freeadddictionary.dict.report.domain.ReportReply;
import com.freeadddictionary.dict.report.dto.request.ReportReplyAddRequest;
import com.freeadddictionary.dict.report.dto.request.ReportReplyUpdateRequest;
import com.freeadddictionary.dict.report.repository.ReportReplyRepository;
import com.freeadddictionary.dict.shared.exception.DataNotFoundException;
import com.freeadddictionary.dict.user.domain.DictUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportReplyService {

  private final ReportReplyRepository reportReplyRepository;

  @Transactional
  public ReportReply create(Report report, ReportReplyAddRequest request, DictUser author) {
    ReportReply reply =
        ReportReply.builder().content(request.getContent()).report(report).author(author).build();
    reportReplyRepository.save(reply);
    return reply;
  }

  public ReportReply getReportReply(Long id) {
    Optional<ReportReply> reportReply = reportReplyRepository.findById(id);
    if (reportReply.isPresent()) {
      return reportReply.get();
    } else {
      throw new DataNotFoundException("Reply not found with id: " + id);
    }
  }

  @Transactional
  public void modify(Long id, ReportReplyUpdateRequest request) {
    ReportReply reply =
        reportReplyRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Reply not found with id: " + id));
    reply.update(request.getContent());
    reportReplyRepository.save(reply);
  }

  @Transactional
  public void delete(ReportReply reply) {
    reportReplyRepository.delete(reply);
  }
}
