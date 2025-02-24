package com.freeadddictionary.dict.repository;

import com.freeadddictionary.dict.domain.Inquiry;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

  Page<Inquiry> findByAuthorEmail(String email, Pageable pageable);

  Page<Inquiry> findByResolved(boolean resolved, Pageable pageable);

  @Query("SELECT COUNT(r) FROM Inquiry r WHERE r.resolved = false")
  long countUnresolvedInquiries();

  @Query("SELECT COUNT(r) FROM Inquiry r WHERE r.createdAt >= :startTime")
  long countTodayNewInquiries(LocalDateTime startTime);
}
