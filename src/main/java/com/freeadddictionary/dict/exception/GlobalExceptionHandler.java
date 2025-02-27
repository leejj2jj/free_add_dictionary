package com.freeadddictionary.dict.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.freeadddictionary.dict.dto.response.ErrorResponse;
import com.freeadddictionary.dict.util.LoggingUtil;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  // ------------------ 도메인 예외 처리 ------------------

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {

    return createErrorResponse(
        ex.getMessage(), "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
      DuplicateResourceException ex, WebRequest request) {

    return createErrorResponse(ex.getMessage(), "DUPLICATE_RESOURCE", HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleForbiddenException(
      ForbiddenException ex, WebRequest request) {

    return createErrorResponse(ex.getMessage(), "FORBIDDEN_ACCESS", HttpStatus.FORBIDDEN, request);
  }

  // ------------------ 검증 예외 처리 ------------------

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      ValidationException ex, WebRequest request) {

    return createErrorResponse(
        ex.getMessage(), "VALIDATION_ERROR", HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, WebRequest request) {

    // 필드 오류 수집
    List<ErrorResponse.FieldError> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

    // 오류 구조화 로깅
    Map<String, Object> logData = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> logData.put(error.getField(), error.getDefaultMessage()));
    LoggingUtil.logStructured("validation.error", logData);

    return createValidationErrorResponse(
        "입력값 검증에 실패했습니다", fieldErrors, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {

    // 제약 조건 위반 오류 수집
    List<ErrorResponse.FieldError> fieldErrors =
        ex.getConstraintViolations().stream()
            .map(
                violation -> {
                  String propertyPath = violation.getPropertyPath().toString();
                  String fieldName =
                      propertyPath.contains(".")
                          ? propertyPath.substring(propertyPath.lastIndexOf(".") + 1)
                          : propertyPath;
                  return new ErrorResponse.FieldError(fieldName, violation.getMessage());
                })
            .collect(Collectors.toList());

    return createValidationErrorResponse(
        "입력값 검증에 실패했습니다", fieldErrors, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {

    String typeName =
        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "알 수 없음";

    String errorMessage =
        String.format("파라미터 '%s'의 값 '%s'가 타입 %s에 유효하지 않습니다", ex.getName(), ex.getValue(), typeName);

    return createErrorResponse(errorMessage, "TYPE_MISMATCH", HttpStatus.BAD_REQUEST, request);
  }

  // ------------------ HTTP 요청 예외 처리 ------------------

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, WebRequest request) {

    return createErrorResponse(
        "필수 파라미터가 누락되었습니다: " + ex.getParameterName(),
        "MISSING_PARAMETER",
        HttpStatus.BAD_REQUEST,
        request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, WebRequest request) {

    String message = "요청 본문을 파싱할 수 없습니다";

    // 보다 구체적인 오류 메시지 작성
    if (ex.getCause() instanceof JsonParseException) {
      message = "JSON 형식이 올바르지 않습니다";
    } else if (ex.getCause() instanceof MismatchedInputException) {
      message = "데이터 형식이 일치하지 않습니다";
    } else if (ex.getCause() instanceof InvalidFormatException) {
      InvalidFormatException ife = (InvalidFormatException) ex.getCause();
      message =
          String.format(
              "잘못된 형식: '%s' 값은 %s 타입으로 변환할 수 없습니다",
              ife.getValue(), ife.getTargetType().getSimpleName());
    }

    return createErrorResponse(message, "INVALID_REQUEST_BODY", HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, WebRequest request) {

    StringBuilder message = new StringBuilder("지원하지 않는 HTTP 메서드입니다: ").append(ex.getMethod());

    if (ex.getSupportedHttpMethods() != null && !ex.getSupportedHttpMethods().isEmpty()) {
      message.append(", 지원되는 메서드: ");
      ex.getSupportedHttpMethods().forEach(method -> message.append(method).append(" "));
    }

    return createErrorResponse(
        message.toString().trim(), "METHOD_NOT_SUPPORTED", HttpStatus.METHOD_NOT_ALLOWED, request);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex, WebRequest request) {

    StringBuilder message =
        new StringBuilder("지원하지 않는 미디어 타입입니다: ").append(ex.getContentType()).append(", 지원되는 타입: ");

    ex.getSupportedMediaTypes().forEach(type -> message.append(type).append(" "));

    return createErrorResponse(
        message.toString().trim(),
        "MEDIA_TYPE_NOT_SUPPORTED",
        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        request);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
      NoHandlerFoundException ex, WebRequest request) {

    return createErrorResponse(
        "요청된 경로를 처리할 수 없습니다: " + ex.getRequestURL(),
        "ENDPOINT_NOT_FOUND",
        HttpStatus.NOT_FOUND,
        request);
  }

  // ------------------ 데이터 예외 처리 ------------------

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException ex, WebRequest request) {

    String message = "데이터 무결성 제약 조건 위반이 발생했습니다";
    String cause = ex.getMostSpecificCause().getMessage();

    // 외래 키 제약 조건 위반 감지
    if (cause != null
        && (cause.contains("foreign key")
            || cause.contains("FK_")
            || cause.contains("referenced"))) {
      message = "다른 데이터에서 참조 중인 데이터는 삭제할 수 없습니다";
    }
    // 중복 데이터 감지
    else if (cause != null
        && (cause.contains("unique")
            || cause.contains("Duplicate entry")
            || cause.contains("UNIQUE_"))) {
      message = "이미 존재하는 데이터입니다";
    }

    return createErrorResponse(message, "DATA_INTEGRITY_VIOLATION", HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<ErrorResponse> handleDateTimeParseException(
      DateTimeParseException ex, WebRequest request) {

    return createErrorResponse(
        "올바르지 않은 날짜/시간 형식입니다: " + ex.getParsedString(),
        "INVALID_DATETIME_FORMAT",
        HttpStatus.BAD_REQUEST,
        request);
  }

  // ------------------ 인증/인가 예외 처리 ------------------

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ex, WebRequest request) {

    String message = "인증에 실패했습니다";
    String errorCode = "AUTHENTICATION_FAILED";

    if (ex instanceof BadCredentialsException) {
      message = "이메일 또는 비밀번호가 올바르지 않습니다";
      errorCode = "INVALID_CREDENTIALS";
    } else if (ex instanceof DisabledException) {
      message = "비활성화된 계정입니다. 이메일 인증을 완료해 주세요";
      errorCode = "ACCOUNT_DISABLED";
    } else if (ex instanceof LockedException) {
      message = "계정이 잠겼습니다. 관리자에게 문의하세요";
      errorCode = "ACCOUNT_LOCKED";
    }

    return createErrorResponse(message, errorCode, HttpStatus.UNAUTHORIZED, request);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex, WebRequest request) {

    return createErrorResponse(
        "이 작업을 수행할 권한이 없습니다", "ACCESS_DENIED", HttpStatus.FORBIDDEN, request);
  }

  // ------------------ 파일 업로드 예외 처리 ------------------

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
      MaxUploadSizeExceededException ex, WebRequest request) {

    return createErrorResponse(
        "파일 크기가 허용된 최대 크기를 초과했습니다",
        "MAX_UPLOAD_SIZE_EXCEEDED",
        HttpStatus.PAYLOAD_TOO_LARGE,
        request);
  }

  // ------------------ 기본 예외 처리 ------------------

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllUncaughtException(
      Exception ex, WebRequest request) {

    String traceId = generateTraceId();

    // 자세한 오류 스택 트레이스 로깅
    log.error("[{}] 예상치 못한 오류 발생: {}", traceId, ex.getMessage(), ex);

    // 구조화된 로깅
    LoggingUtil.logStructured(
        "error.uncaught",
        Map.of(
            "exceptionType",
            ex.getClass().getName(),
            "message",
            ex.getMessage() != null ? ex.getMessage() : "null",
            "traceId",
            traceId));

    // 클라이언트에게는 상세 정보 없이 일반적인 오류 메시지만 전달
    return createErrorResponse(
        "서버에서 오류가 발생했습니다. 나중에 다시 시도해 주세요",
        "INTERNAL_SERVER_ERROR",
        HttpStatus.INTERNAL_SERVER_ERROR,
        request,
        traceId);
  }

  // ------------------ 헬퍼 메서드 ------------------

  /** 표준 오류 응답을 생성합니다. */
  private ResponseEntity<ErrorResponse> createErrorResponse(
      String message, String errorCode, HttpStatus status, WebRequest request) {

    return createErrorResponse(message, errorCode, status, request, generateTraceId());
  }

  private ResponseEntity<ErrorResponse> createErrorResponse(
      String message, String errorCode, HttpStatus status, WebRequest request, String traceId) {

    String path = getRequestPath(request);

    // 로그 레벨은 HTTP 상태 코드에 따라 결정
    if (status.is5xxServerError()) {
      log.error("[{}] {} (code: {}, path: {})", traceId, message, errorCode, path);
    } else {
      log.warn("[{}] {} (code: {}, path: {})", traceId, message, errorCode, path);
    }

    ErrorResponse errorResponse =
        new ErrorResponse(message, errorCode, status.value()).withPath(path).withTraceId(traceId);

    return ResponseEntity.status(status).body(errorResponse);
  }

  /** 필드 오류가 있는 검증 실패 응답을 생성합니다. */
  private ResponseEntity<ErrorResponse> createValidationErrorResponse(
      String message,
      List<ErrorResponse.FieldError> fieldErrors,
      HttpStatus status,
      WebRequest request) {

    String path = getRequestPath(request);
    String traceId = generateTraceId();

    log.warn("[{}] {} (path: {}, fields: {})", traceId, message, path, fieldErrors.size());

    ErrorResponse errorResponse =
        new ErrorResponse(message, "VALIDATION_FAILED", status.value())
            .withPath(path)
            .withTraceId(traceId);

    // 필드 오류 추가
    fieldErrors.forEach(
        fieldError -> errorResponse.addFieldError(fieldError.getField(), fieldError.getMessage()));

    return ResponseEntity.status(status).body(errorResponse);
  }

  /** 요청 경로를 추출합니다. */
  private String getRequestPath(WebRequest request) {
    if (request instanceof ServletWebRequest) {
      return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
    return "unknown";
  }

  /** 오류 추적을 위한 고유 ID를 생성합니다. */
  private String generateTraceId() {
    return UUID.randomUUID().toString().substring(0, 8);
  }
}
