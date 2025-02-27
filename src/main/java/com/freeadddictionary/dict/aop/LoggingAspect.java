package com.freeadddictionary.dict.aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeadddictionary.dict.annotation.LogExecutionTime;
import com.freeadddictionary.dict.annotation.SensitiveData;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  private final ObjectMapper objectMapper;

  // 민감한 데이터 타입(비밀번호 등) 탐지용 키워드
  private static final String[] SENSITIVE_KEYWORDS = {
    "password", "pwd", "secret", "token", "key", "credential", "auth"
  };

  public LoggingAspect() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Around(
      "execution(* com.freeadddictionary.dict.service.*.*(..)) ||"
          + " execution(* com.freeadddictionary.dict.controller.*.*(..)) ||"
          + " @annotation(com.freeadddictionary.dict.annotation.LogExecutionTime)")
  public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    String transactionId = UUID.randomUUID().toString().substring(0, 8);
    MDC.put("txId", transactionId);

    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    String methodName = method.getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    boolean isAnnotated = method.isAnnotationPresent(LogExecutionTime.class);

    // 메서드 파라미터 로깅
    String paramInfo = buildParamInfo(joinPoint, method);
    log.debug(
        "[{}] Starting {} in {} with params: {}", transactionId, methodName, className, paramInfo);

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      Object result = joinPoint.proceed();
      stopWatch.stop();
      long executionTime = stopWatch.getTotalTimeMillis();

      // 반환값 로깅 (객체가 너무 크면 요약 정보만)
      String resultInfo = extractResultInfo(result);

      // 로그 레벨 결정 - 애너테이션이 있거나 시간이 오래 걸릴 때 경고 표시
      if (executionTime > 1000 || isAnnotated) {
        log.warn(
            "[{}] Long execution time - {} in {}: {}ms, result: {}",
            transactionId,
            methodName,
            className,
            executionTime,
            resultInfo);
      } else {
        log.debug(
            "[{}] Completed {} in {} in {}ms, result: {}",
            transactionId,
            methodName,
            className,
            executionTime,
            resultInfo);
      }

      return result;
    } catch (Exception e) {
      stopWatch.stop();
      log.error(
          "[{}] Exception in {} - {}: {} ({}ms)",
          transactionId,
          className,
          methodName,
          e.getMessage(),
          stopWatch.getTotalTimeMillis(),
          e);
      throw e;
    } finally {
      MDC.remove("txId");
    }
  }

  private String buildParamInfo(ProceedingJoinPoint joinPoint, Method method) {
    try {
      Object[] args = joinPoint.getArgs();
      Parameter[] parameters = method.getParameters();

      if (args == null || args.length == 0) {
        return "no parameters";
      }

      Map<String, Object> paramMap = new HashMap<>();
      IntStream.range(0, args.length)
          .forEach(
              i -> {
                if (i < parameters.length) {
                  String paramName = parameters[i].getName();
                  Object value = args[i];

                  // 민감 데이터 처리
                  if (isSensitiveParameter(parameters[i], paramName)) {
                    paramMap.put(paramName, "******");
                  } else {
                    try {
                      // 간단한 타입은 값을 그대로, 복잡한 객체는 클래스 이름으로 표현 (요약)
                      if (isPrimitiveOrString(value)) {
                        paramMap.put(paramName, value);
                      } else {
                        paramMap.put(
                            paramName,
                            value != null
                                ? value.getClass().getSimpleName()
                                    + "@"
                                    + Integer.toHexString(value.hashCode())
                                : "null");
                      }
                    } catch (Exception e) {
                      paramMap.put(paramName, "Error extracting value: " + e.getMessage());
                    }
                  }
                }
              });

      return objectMapper.writeValueAsString(paramMap);
    } catch (Exception e) {
      return "Error logging parameters: " + e.getMessage();
    }
  }

  private boolean isSensitiveParameter(Parameter parameter, String paramName) {
    // 1. @SensitiveData 어노테이션으로 명시된 경우
    if (parameter.isAnnotationPresent(SensitiveData.class)) {
      return true;
    }

    // 2. 파라미터 이름에 민감한 키워드가 포함된 경우
    String lowerParamName = paramName.toLowerCase();
    return Arrays.stream(SENSITIVE_KEYWORDS).anyMatch(keyword -> lowerParamName.contains(keyword));
  }

  private String extractResultInfo(Object result) {
    try {
      if (result == null) {
        return "null";
      }

      if (isPrimitiveOrString(result)) {
        return result.toString();
      }

      // 컬렉션인 경우 크기만 표시
      if (result instanceof java.util.Collection) {
        return result.getClass().getSimpleName()
            + " (size="
            + ((java.util.Collection<?>) result).size()
            + ")";
      }

      // 너무 복잡한 객체는 클래스 이름과 해시코드만 표시
      return result.getClass().getSimpleName() + "@" + Integer.toHexString(result.hashCode());
    } catch (Exception e) {
      return "Error extracting result: " + e.getMessage();
    }
  }

  private boolean isPrimitiveOrString(Object obj) {
    if (obj == null) return true;

    Class<?> clazz = obj.getClass();
    return clazz.isPrimitive()
        || clazz == String.class
        || clazz == Boolean.class
        || clazz == Character.class
        || Number.class.isAssignableFrom(clazz)
        || clazz == java.util.Date.class
        || clazz == java.time.LocalDate.class
        || clazz == java.time.LocalDateTime.class;
  }
}
