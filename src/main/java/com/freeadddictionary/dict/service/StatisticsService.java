package com.freeadddictionary.dict.service;

import com.freeadddictionary.dict.dto.response.StatisticsResponse;
import com.freeadddictionary.dict.repository.DictionaryRepository;
import com.freeadddictionary.dict.repository.InquiryRepository;
import com.freeadddictionary.dict.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

  private final UserRepository userRepository;
  private final DictionaryRepository dictionaryRepository;
  private final InquiryRepository inquiryRepository;

  @Cacheable(value = "statistics", key = "'daily'")
  public StatisticsResponse getStatistics() {
    LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

    return StatisticsResponse.builder()
        .totalUsers(userRepository.countTotalUsers())
        .totalDictionaries(dictionaryRepository.count())
        .totalInquiries(inquiryRepository.count())
        .unresolvedInquiries(inquiryRepository.countUnresolvedInquiries())
        .todayNewUsers(userRepository.countTodayNewUsers(startOfDay))
        .todayNewDictionaries(dictionaryRepository.countTodayNewDictionaries(startOfDay))
        .todayNewInquiries(inquiryRepository.countTodayNewInquiries(startOfDay))
        .build();
  }

  @CacheEvict(value = "statistics", key = "'daily'")
  @Scheduled(fixedDelay = 300000)
  public void refreshStatistics() {}
}
