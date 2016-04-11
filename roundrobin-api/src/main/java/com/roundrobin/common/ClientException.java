package com.roundrobin.common;

public class ClientException extends IllegalArgumentException {

  private static final long serialVersionUID = 1L;
  private ErrorCodes code;

  public ClientException(ErrorCodes code) {
    super(code.toString());
    this.code = code;
  }

  public ClientException(ErrorCodes code, Throwable e) {
    super(e);
    this.code = code;
  }

  public ClientException(ErrorCodes code, String message, Throwable e) {
    super(message, e);
    this.code = code;
  }

  public ErrorCodes getCode() {
    return code;
  }

  public void setCode(ErrorCodes code) {
    this.code = code;
  }

}
