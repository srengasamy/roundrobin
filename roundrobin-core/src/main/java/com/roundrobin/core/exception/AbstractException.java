package com.roundrobin.core.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException {

  private static final long serialVersionUID = -2350337680024841058L;

  protected String errorType;
  protected String errorCode;
  protected String param;
  protected HttpStatus httpStatus;

  public AbstractException(String errorType, String errorCode, HttpStatus httpStatus) {
    super(errorCode.toString());
    this.errorType = errorType;
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }

  public AbstractException(String errorType, String errorCode, String param, HttpStatus httpStatus) {
    super(errorCode.toString());
    this.errorType = errorType;
    this.errorCode = errorCode;
    this.param = param;
    this.httpStatus = httpStatus;
  }

  public AbstractException(String errorType, String errorCode, HttpStatus httpStatus, Throwable t) {
    super(errorCode.toString(), t);
    this.errorType = errorType;
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }

  public AbstractException(String errorType, String errorCode, String param, HttpStatus httpStatus, Throwable t) {
    super(errorCode.toString(), t);
    this.errorType = errorType;
    this.errorCode = errorCode;
    this.param = param;
    this.httpStatus = httpStatus;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorType() {
    return errorType;
  }

  public void setErrorType(String errorType) {
    this.errorType = errorType;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

}
