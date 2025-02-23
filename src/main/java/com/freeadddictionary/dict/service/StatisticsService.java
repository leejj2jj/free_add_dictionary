package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.dto.response.StatisticsResponse;
import com.freeadddictionary.dict.repository.DictionaryRepository;
import com.freeadddictionary.dict.repository.ReportRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

  private final UserRepository userRepository;
  private final DictionaryRepository dictionaryRepository;
  private final ReportRepository reportRepository;

  @Cacheable(value = "statistics", key = "'daily'")
  public StatisticsResponse getStatistics() {
    LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

    return StatisticsResponse.builder()
        .totalUsers(userRepository.count())
        .totalDictionaries(dictionaryRepository.count())
        .totalReports(reportRepository.count())
        .unresolvedReports(reportRepository.countUnresolvedReports())
        .todayNewUsers(userRepository.countTodayNewUsers(startOfDay))
        .todayNewDictionaries(dictionaryRepository.countTodayNewDictionaries(startOfDay))
        .todayNewReports(reportRepository.countTodayNewReports(startOfDay))
        .build();
  }
}
