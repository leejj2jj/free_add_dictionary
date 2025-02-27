package com.freeadddictionary.dict.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtil {

  private static final Logger log = LoggerFactory.getLogger(LoggingUtil.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

  /**
   * 구조화된 JSON 형태로 로그를 기록합니다.
   *
   * @param event 이벤트 유형 (예: dictionary.created, user.login)
   * @param data 기록할 데이터가 포함된 맵
   */
  public static void logStructured(String event, Map<String, Object> data) {
    try {
      Map<String, Object> logData = new HashMap<>(data);
      logData.put("event", event);
      logData.put("timestamp", LocalDateTime.now().format(formatter));
      log.info(objectMapper.writeValueAsString(logData));
    } catch (Exception e) {
      log.error("Failed to log structured data", e);
    }
  }

  /**
   * 애플리케이션 감사(audit) 목적의 이벤트를 기록합니다.
   *
   * @param action 수행된 작업 (예: CREATE, UPDATE, DELETE)
   * @param resourceType 리소스 유형 (예: Dictionary, User)
   * @param resourceId 리소스 ID
   * @param actor 작업 수행자 (일반적으로 사용자 이메일)
   * @param details 추가 세부 정보
   */
  public static void logAudit(
      String action,
      String resourceType,
      Object resourceId,
      String actor,
      Map<String, Object> details) {
    Map<String, Object> auditData = new HashMap<>();
    auditData.put("action", action);
    auditData.put("resourceType", resourceType);
    auditData.put("resourceId", resourceId);
    auditData.put("actor", actor);

    if (details != null) {
      auditData.put("details", details);
    }

    logStructured("audit", auditData);
  }
}
