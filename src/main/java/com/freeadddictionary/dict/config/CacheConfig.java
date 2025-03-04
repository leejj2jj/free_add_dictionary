package com.freeadddictionary.dict.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(
        Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(100));

    cacheManager.setCacheNames(
        java.util.Arrays.asList("statistics", "dictionary", "inquiries", "admins"));

    return cacheManager;
  }

  @Bean
  public Caffeine caffeineConfig() {
    return Caffeine.newBuilder()
        .expireAfterWrite(60, TimeUnit.MINUTES)
        .initialCapacity(100)
        .maximumSize(500);
  }
}
