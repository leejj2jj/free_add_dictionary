package com.freeadddictionary.dict.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATETIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final DateTimeFormatter FRIENDLY_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

  private DateTimeUtil() {}

  /**
   * LocalDateTime을 "yyyy-MM-dd" 형식의 문자열로 변환합니다.
   *
   * @param dateTime 변환할 날짜/시간
   * @return 형식화된 날짜 문자열, null인 경우 빈 문자열
   */
  public static String formatDate(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(DATE_FORMATTER) : "";
  }

  /**
   * LocalDateTime을 "yyyy-MM-dd HH:mm:ss" 형식의 문자열로 변환합니다.
   *
   * @param dateTime 변환할 날짜/시간
   * @return 형식화된 날짜/시간 문자열, null인 경우 빈 문자열
   */
  public static String formatDateTime(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : "";
  }

  /**
   * LocalDateTime을 친화적인 표시 형식 ("yyyy년 MM월 dd일 HH:mm")으로 변환합니다.
   *
   * @param dateTime 변환할 날짜/시간
   * @return 친화적 형식의 날짜/시간 문자열, null인 경우 빈 문자열
   */
  public static String formatFriendly(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(FRIENDLY_FORMATTER) : "";
  }

  /**
   * 문자열이 유효한 날짜/시간 형식("yyyy-MM-dd HH:mm:ss")인지 확인합니다.
   *
   * @param dateTimeStr 검사할 날짜/시간 문자열
   * @return 유효한 경우 true, 그렇지 않으면 false
   */
  public static boolean isValidDateTime(String dateTimeStr) {
    try {
      if (dateTimeStr == null || dateTimeStr.isEmpty()) {
        return false;
      }
      LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 지정된 날짜/시간이 현재 시간 기준으로 만료되었는지 확인합니다.
   *
   * @param expiryDateTime 만료 여부를 확인할 날짜/시간
   * @return 만료된 경우 true, 그렇지 않으면 false
   */
  public static boolean isExpired(LocalDateTime expiryDateTime) {
    return expiryDateTime != null && LocalDateTime.now().isAfter(expiryDateTime);
  }
}
