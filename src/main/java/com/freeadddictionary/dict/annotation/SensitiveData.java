package com.freeadddictionary.dict.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 로깅 시 마스킹 처리 해야 하는 민감한 데이터를 표시하는 애너테이션입니다. */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {}
