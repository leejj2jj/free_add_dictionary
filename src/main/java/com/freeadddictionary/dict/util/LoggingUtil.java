package com.freeadddictionary.dict.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingUtil {

  private static final List<String> EXCLUDE_PATHS =
      Arrays.asList("/health", "/static/", "/favicon.ico");

  public static void logRequest(HttpServletRequest request, String body) {
    if (shouldLog(request.getRequestURI())) {
      log.info("Request URI: {} {}", request.getMethod(), request.getRequestURI());
      log.info("Request Headers: {}", getHeadersAsString(request));
      if (body != null && !body.isEmpty()) {
        log.info("Request Body: {}", body);
      }
    }
  }

  private static boolean shouldLog(String uri) {
    return EXCLUDE_PATHS.stream().noneMatch(uri::startsWith);
  }

  private static String getHeadersAsString(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames()).stream()
        .map(headerName -> headerName + ": " + request.getHeader(headerName))
        .collect(Collectors.joining(", "));
  }

  private LoggingUtil() {
    throw new IllegalStateException("Utility class");
  }
}
