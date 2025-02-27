package com.freeadddictionary.dict.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  // 기본 오류 정보
  private final String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime timestamp = LocalDateTime.now();

  // 추가 정보
  private String errorCode;
  private Integer status;
  private String path;

  // 요청 정보 (디버깅에 유용)
  private Map<String, String> request;

  // 필드 오류 목록 (유효성 검증 실패 시)
  private List<FieldError> errors;

  // 오류 추적 식별자 (로깅과 연계)
  private String traceId;

  public ErrorResponse(String message) {
    this.message = message;
  }

  public ErrorResponse(String message, String errorCode, Integer status) {
    this.message = message;
    this.errorCode = errorCode;
    this.status = status;
  }

  public ErrorResponse withPath(String path) {
    this.path = path;
    return this;
  }

  public ErrorResponse withTraceId(String traceId) {
    this.traceId = traceId;
    return this;
  }

  public ErrorResponse withRequest(String method, String uri, String contentType) {
    if (this.request == null) {
      this.request = new LinkedHashMap<>();
    }
    this.request.put("method", method);
    this.request.put("uri", uri);
    if (contentType != null) {
      this.request.put("contentType", contentType);
    }
    return this;
  }

  public void addFieldError(String field, String message) {
    if (errors == null) {
      errors = new ArrayList<>();
    }
    errors.add(new FieldError(field, message));
  }

  @Getter
  public static class FieldError {
    private final String field;
    private final String message;

    public FieldError(String field, String message) {
      this.field = field;
      this.message = message;
    }
  }
}
