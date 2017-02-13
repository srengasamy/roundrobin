package com.roundrobin.core.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
public class Error {
  private String type;
  private String code;
  private String message;
  private String param;

  @JsonProperty("field_errors")
  private List<String> fieldErrors;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

  public List<String> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<String> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

}
