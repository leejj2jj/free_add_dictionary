package com.freeadddictionary.dict.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (handler instanceof HandlerMethod) {
      String methodName = ((HandlerMethod) handler).getMethod().getName();
      log.info("Starting {} - {}", request.getMethod(), methodName);
      request.setAttribute("startTime", System.currentTimeMillis());
    }
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    if (handler instanceof HandlerMethod) {
      long startTime = (Long) request.getAttribute("startTime");
      long endTime = System.currentTimeMillis();
      log.info(
          "Completed {} - {} in {}ms with status {}",
          request.getMethod(),
          ((HandlerMethod) handler).getMethod().getName(),
          (endTime - startTime),
          response.getStatus());
    }
  }
}
