package com.roundrobin.exception;

public class ClientException extends GeneralException {

  private static final long serialVersionUID = 4665827751058454341L;
  private String code;

  public ClientException(String code, String message) {
    super(message);
    this.code = code;
  }

  public ClientException(String code, String message, Throwable t) {
    super(message, t);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


}
