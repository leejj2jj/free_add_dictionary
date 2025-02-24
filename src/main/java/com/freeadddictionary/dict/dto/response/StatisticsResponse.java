package com.freeadddictionary.dict.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatisticsResponse {

  private final long totalUsers;
  private final long totalDictionaries;
  private final long totalInquiries;
  private final long unresolvedInquiries;
  private final long todayNewUsers;
  private final long todayNewDictionaries;
  private final long todayNewInquiries;
}
