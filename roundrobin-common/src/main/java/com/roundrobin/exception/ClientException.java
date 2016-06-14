package com.roundrobin.exception;

public class ClientException extends GeneralException {

  private static final long serialVersionUID = 4665827751058454341L;
  private int code;

  public ClientException(int code) {
    super(code + "");
    this.code = code;
  }

  public ClientException(int code, String message) {
    super(message);
    this.code = code;
  }

  public ClientException(int code, String message, Throwable t) {
    super(message, t);
    this.code = code;
  }

  @Override
  public int getCode() {
    return code;
  }

}
