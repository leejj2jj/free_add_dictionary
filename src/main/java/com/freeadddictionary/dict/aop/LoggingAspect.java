package com.freeadddictionary.dict.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Around("execution(* com.freeadddictionary.dict.service.*.*(..))")
  public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();

    log.info("Starting {} in {}", methodName, className);
    long start = System.currentTimeMillis();

    try {
      Object result = joinPoint.proceed();
      long executionTime = System.currentTimeMillis() - start;
      log.info("Completed {} in {} in {}ms", methodName, className, executionTime);
      return result;
    } catch (Exception e) {
      log.error("Exception in {} - {}: {}", className, methodName, e.getMessage());
      throw e;
    }
  }
}
