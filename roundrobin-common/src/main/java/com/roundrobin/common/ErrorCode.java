package com.roundrobin.common;

public enum ErrorCode {
  INVALID_FIELD(10001),
  INVALID_URL(10002),
  UNPARSABLE_INPUT(10003),
  UNKNOWN_PROFILE(10004),
  INTERNAL_ERROR(5000);

  private int code;

  private ErrorCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

}
