package com.freeadddictionary.dict.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static String formatDate(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(DATE_FORMATTER) : "";
  }

  public static String formatDateTime(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : "";
  }

  public static LocalDateTime startOfDay() {
    return LocalDate.now().atStartOfDay();
  }

  public static LocalDateTime endOfDay() {
    return LocalDate.now().atTime(23, 59, 59);
  }

  private DateTimeUtil() {
    throw new IllegalStateException("Utility class");
  }
}
