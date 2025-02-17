package com.freeadddictionary.dict.report.repository;

import com.freeadddictionary.dict.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

  @Query(
      "select "
          + "distinct r "
          + "from Report r "
          + "left outer join DictUser u1 on r.author = u1 "
          + "left outer join ReportReply rr on rr.report = r "
          + "left outer join DictUser u2 on r.author = u2 "
          + "where "
          + "r.title like %:kw% "
          + "or r.content like %:kw% "
          + "   or u1.username like %:kw% "
          + "   or rr.content like %:kw% "
          + "   or u2.username like %:kw% ")
  Page<Report> searchByKeyword(@Param("kw") String kw, Pageable pageable);
}
