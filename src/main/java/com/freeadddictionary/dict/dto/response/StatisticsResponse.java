package com.freeadddictionary.dict.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatisticsResponse {

  private final long totalUsers;
  private final long totalDictionaries;
  private final long totalReports;
  private final long unresolvedReports;
  private final long todayNewUsers;
  private final long todayNewDictionaries;
  private final long todayNewReports;
}
