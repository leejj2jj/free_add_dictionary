package com.freeadddictionary.dict.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.freeadddictionary.dict.dto.response.ErrorResponse;
import com.freeadddictionary.dict.util.LoggingUtil;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.naming.ServiceUnavailableException;
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
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
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
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // API 관련 예외 - JSON 응답 제공

  // 리소스를 찾을 수 없음
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    log.warn("Resource not found: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
  }

  // 중복 리소스
  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
      DuplicateResourceException ex) {
    log.warn("Duplicate resource: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
  }

  // 권한 없음
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
    log.warn("Forbidden access: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.getMessage()));
  }

  // 유효성 검증 실패
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
    log.warn("Validation failed: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  // 메서드 인자 유효성 검증 실패
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, WebRequest request) {

    // 필드 오류 정보 수집
    ErrorResponse errorResponse =
        new ErrorResponse("입력값 검증에 실패했습니다", "VALIDATION_FAILED", HttpStatus.BAD_REQUEST.value());

    // 개별 필드 오류 추가
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errorResponse.addFieldError(error.getField(), error.getDefaultMessage()));

    // 요청 경로 추가
    errorResponse.withPath(getRequestPath(request));

    // 로그 구조화
    Map<String, Object> logData = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> logData.put(error.getField(), error.getDefaultMessage()));
    LoggingUtil.logStructured("validation.error", logData);

    log.warn(
        "Method argument validation failed: {}", ex.getBindingResult().getFieldErrors().size());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  // 제약 조건 위반
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    String errorMessage =
        ex.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining(", "));

    log.warn("Constraint violation: {}", errorMessage);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("제약 조건 위반: " + errorMessage));
  }

  // 메서드 인자 타입 불일치
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    String errorMessage =
        String.format(
            "파라미터 '%s'의 값 '%s'가 타입 %s에 유효하지 않습니다",
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

    log.warn("Method argument type mismatch: {}", errorMessage);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
  }

  // 요청 파라미터 누락
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex) {
    log.warn("Missing request parameter: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("필수 파라미터 누락: " + ex.getParameterName()));
  }

  // 요청 본문 파싱 실패
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {
    log.warn("HTTP message not readable: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("요청 본문을 파싱할 수 없습니다"));
  }

  // HTTP 메서드 지원하지 않음
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex) {
    log.warn("HTTP method not supported: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(new ErrorResponse("지원하지 않는 HTTP 메서드입니다: " + ex.getMethod()));
  }

  // 미디어 타입 지원하지 않음
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex) {
    log.warn("Media type not supported: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        .body(new ErrorResponse("지원하지 않는 미디어 타입입니다: " + ex.getContentType()));
  }

  // 핸들러를 찾을 수 없음
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    log.warn("No handler found: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("요청된 URL을 처리할 수 없습니다: " + ex.getRequestURL()));
  }

  // 데이터 무결성 위반
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException ex) {
    log.error("Data integrity violation: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("데이터 무결성 제약 조건 위반이 발생했습니다"));
  }

  // 인증 관련 예외
  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
    log.warn("Authentication error: {}", ex.getMessage());

    String message = "인증 실패";
    HttpStatus status = HttpStatus.UNAUTHORIZED;

    if (ex instanceof BadCredentialsException) {
      message = "잘못된 인증 정보입니다";
    } else if (ex instanceof LockedException) {
      message = "계정이 잠겼습니다";
    } else if (ex instanceof DisabledException) {
      message = "계정이 비활성화되었습니다";
    }

    return ResponseEntity.status(status).body(new ErrorResponse(message));
  }

  // 인가 관련 예외
  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    log.warn("Access denied: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorResponse("이 작업을 수행할 권한이 없습니다"));
  }

  // CSRF 토큰 관련 예외
  @ExceptionHandler({InvalidCsrfTokenException.class, MissingCsrfTokenException.class})
  public ResponseEntity<ErrorResponse> handleCsrfException(Exception ex) {
    log.warn("CSRF error: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorResponse("CSRF 보안 검증에 실패했습니다. 페이지를 새로고침 후 다시 시도해 주세요."));
  }

  // 그 외 모든 예외
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex) {
    log.error("Uncaught exception", ex);

    // 개발 모드에서만 상세 오류 제공
    String message = "서버에서 오류가 발생했습니다. 나중에 다시 시도해 주세요.";

    LoggingUtil.logStructured(
        "error.uncaught",
        Map.of(
            "exceptionType",
            ex.getClass().getName(),
            "message",
            ex.getMessage() != null ? ex.getMessage() : "null"));

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(message));
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, WebRequest request) {

    log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());

    ErrorResponse errorResponse =
        new ErrorResponse(ex.getMessage(), ex.getErrorCode(), ex.getStatus().value());

    return ResponseEntity.status(ex.getStatus())
        .body(errorResponse.withPath(getRequestPath(request)));
  }

  // JSON 파싱 예외
  private String getRequestPath(WebRequest request) {
    return ((ServletWebRequest) request).getRequest().getRequestURI();
  }

  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<ErrorResponse> handleJsonParseException(
      JsonParseException ex, WebRequest request) {
    String traceId = UUID.randomUUID().toString();
    log.warn("JSON Parse Exception [{}]: {}", traceId, ex.getMessage());

    ErrorResponse errorResponse =
        new ErrorResponse(
            "JSON 형식이 올바르지 않습니다", "INVALID_JSON_FORMAT", HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errorResponse.withTraceId(traceId).withPath(getRequestPath(request)));
  }

  // JSON 매핑 예외
  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<ErrorResponse> handleJsonMappingException(
      JsonMappingException ex, WebRequest request) {
    String traceId = UUID.randomUUID().toString();
    log.warn("JSON Mapping Exception [{}]: {}", traceId, ex.getMessage());

    String message = "JSON 데이터를 객체에 매핑할 수 없습니다";
    String errorCode = "JSON_MAPPING_ERROR";

    // 보다 구체적인 오류 메시지 제공
    if (ex instanceof InvalidFormatException) {
      InvalidFormatException ife = (InvalidFormatException) ex;
      message =
          String.format(
              "잘못된 형식: '%s' 값은 %s 타입으로 변환할 수 없습니다",
              ife.getValue(), ife.getTargetType().getSimpleName());
      errorCode = "INVALID_DATA_FORMAT";
    } else if (ex instanceof MismatchedInputException) {
      message = "데이터 형식이 일치하지 않습니다";
    }

    ErrorResponse errorResponse =
        new ErrorResponse(message, errorCode, HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errorResponse.withTraceId(traceId).withPath(getRequestPath(request)));
  }

  // 파일 업로드 크기 제한 초과 예외
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
      MaxUploadSizeExceededException ex, WebRequest request) {
    log.warn("Max upload size exceeded: {}", ex.getMessage());

    ErrorResponse errorResponse =
        new ErrorResponse(
            "파일 크기가 허용된 최대 크기를 초과했습니다",
            "MAX_UPLOAD_SIZE_EXCEEDED",
            HttpStatus.PAYLOAD_TOO_LARGE.value());

    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(errorResponse.withPath(getRequestPath(request)));
  }

  // 멀티파트 요청 처리 예외
  @ExceptionHandler(MultipartException.class)
  public ResponseEntity<ErrorResponse> handleMultipartException(
      MultipartException ex, WebRequest request) {
    log.warn("Multipart exception: {}", ex.getMessage());

    ErrorResponse errorResponse =
        new ErrorResponse(
            "파일 업로드 처리 중 오류가 발생했습니다", "MULTIPART_ERROR", HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errorResponse.withPath(getRequestPath(request)));
  }

  // 날짜/시간 파싱 예외
  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<ErrorResponse> handleDateTimeParseException(
      DateTimeParseException ex, WebRequest request) {
    log.warn("DateTime parse exception: {}", ex.getMessage());

    ErrorResponse errorResponse =
        new ErrorResponse(
            "올바르지 않은 날짜/시간 형식입니다: " + ex.getParsedString(),
            "INVALID_DATETIME_FORMAT",
            HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errorResponse.withPath(getRequestPath(request)));
  }

  // I/O 예외
  @ExceptionHandler(IOException.class)
  public ResponseEntity<ErrorResponse> handleIOException(IOException ex, WebRequest request) {
    String traceId = UUID.randomUUID().toString();
    log.error("I/O Exception [{}]: {}", traceId, ex.getMessage(), ex);

    ErrorResponse errorResponse =
        new ErrorResponse(
            "파일 처리 중 오류가 발생했습니다", "IO_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse.withTraceId(traceId).withPath(getRequestPath(request)));
  }

  // 서비스 접근 불가 예외
  @ExceptionHandler(ServiceUnavailableException.class)
  public ResponseEntity<ErrorResponse> handleServiceUnavailableException(
      ServiceUnavailableException ex, WebRequest request) {
    String traceId = UUID.randomUUID().toString();
    log.error("Service unavailable [{}]: {}", traceId, ex.getMessage());

    ErrorResponse errorResponse =
        new ErrorResponse(
            "서비스를 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해주세요",
            "SERVICE_UNAVAILABLE",
            HttpStatus.SERVICE_UNAVAILABLE.value());

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(errorResponse.withTraceId(traceId).withPath(getRequestPath(request)));
  }
}
