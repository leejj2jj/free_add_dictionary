package com.freeadddictionary.dict.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

  private final String resourceName;
  private final String fieldName;
  private final Object fieldValue;

  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(
        String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue),
        HttpStatus.NOT_FOUND,
        "RESOURCE_NOT_FOUND");
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public String getResourceName() {
    return resourceName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public Object getFieldValue() {
    return fieldValue;
  }
}
