package com.roundrobin.api;

import java.util.Objects;

import com.roundrobin.common.ErrorCode;

public class Error {
  private ErrorCode code;
  private String message;

  public Error() {

  }

  public Error(ErrorCode code, String message) {
    this.code = code;
    this.message = message;
  }

  public ErrorCode getCode() {
    return code;
  }

  public void setCode(ErrorCode code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return code + ":" + message;
  }

  @Override
  public boolean equals(Object obj) {
    Error error = (Error) obj;
    if (error == null) {
      return false;
    }
    return Objects.equals(code, error.getCode()) && Objects.equals(message, error.getMessage());
  }


}
