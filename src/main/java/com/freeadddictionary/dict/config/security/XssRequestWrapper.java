package com.freeadddictionary.dict.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;

/** XSS(Cross Site Scripting) 공격을 방지하기 위한 HTTP 요청 래퍼 클래스입니다. 요청 파라미터와 헤더에서 잠재적인 XSS 코드를 필터링합니다. */
public class XssRequestWrapper extends HttpServletRequestWrapper {

  private static final Pattern[] XSS_PATTERNS = {
    // 스크립트 태그
    Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
    // src='...'
    Pattern.compile(
        "src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    Pattern.compile(
        "src[\r\n]*=[\r\n]*\"(.*?)\"",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // eval(...)
    Pattern.compile(
        "eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // expression(...)
    Pattern.compile(
        "expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // javascript:...
    Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
    // vbscript:...
    Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
    // onload=...
    Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // onerror=...
    Pattern.compile("onerror(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    // onclick=...
    Pattern.compile("onclick(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
  };

  public XssRequestWrapper(HttpServletRequest request) {
    super(request);
  }

  @Override
  public String[] getParameterValues(String parameter) {
    String[] values = super.getParameterValues(parameter);
    if (values == null) {
      return null;
    }

    int count = values.length;
    String[] encodedValues = new String[count];
    for (int i = 0; i < count; i++) {
      encodedValues[i] = stripXss(values[i]);
    }

    return encodedValues;
  }

  @Override
  public String getParameter(String parameter) {
    String value = super.getParameter(parameter);
    return stripXss(value);
  }

  @Override
  public String getHeader(String name) {
    String value = super.getHeader(name);
    return stripXss(value);
  }

  /**
   * 입력 문자열에서 잠재적인 XSS 공격 벡터를 제거합니다.
   *
   * @param value 처리할 입력 문자열
   * @return XSS 필터링된 문자열
   */
  private String stripXss(String value) {
    if (value == null) {
      return null;
    }

    // 정규식 패턴을 사용하여 XSS 공격 벡터 제거
    String stripped = value;

    for (Pattern pattern : XSS_PATTERNS) {
      stripped = pattern.matcher(stripped).replaceAll("");
    }

    // HTML 엔티티로 변환
    stripped = StringEscapeUtils.escapeHtml4(stripped);

    return stripped;
  }
}
