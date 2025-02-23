package com.freeadddictionary.dict.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

  @Around("@annotation(com.freeadddictionary.dict.annotation.LogExecutionTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();

    try {
      Object result = joinPoint.proceed();
      long executionTime = System.currentTimeMillis() - start;

      if (executionTime > 1000) { // 1초 이상 걸리는 메서드 로깅
        log.warn("Long execution time - {} in {}: {}ms", methodName, className, executionTime);
      }

      return result;
    } catch (Exception e) {
      log.error(
          "Exception during performance logging - {} in {}: {}",
          methodName,
          className,
          e.getMessage());
      throw e;
    }
  }
}
