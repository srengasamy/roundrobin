package com.roundrobin.exception;

public class ServerException extends AbstractException {
  private static final long serialVersionUID = 1L;
  private Integer code;

  public ServerException(Integer code) {
    super(code.toString());
    this.code = code;
  }

  public ServerException(Integer code, String message) {
    super(message);
    this.code = code;
  }

  public ServerException(Integer code, String message, Throwable t) {
    super(message, t);
    this.code = code;
  }

  @Override
  public int getCode() {
    return code;
  }
}
