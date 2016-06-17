package com.roundrobin.api;

import java.util.Objects;

public class Error {
  private int code;
  private String message;

  public Error() {

  }

  public Error(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
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
