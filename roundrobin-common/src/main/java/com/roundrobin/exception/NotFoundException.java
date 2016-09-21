package com.roundrobin.exception;

import static com.roundrobin.error.ErrorType.INVALID_REQUEST_ERROR;

import org.springframework.http.HttpStatus;
public class NotFoundException extends AbstractException {
  private static final long serialVersionUID = 8912137173577075314L;

  public NotFoundException(String errorCode) {
    super(INVALID_REQUEST_ERROR, errorCode, HttpStatus.NOT_FOUND);
  }

  public NotFoundException(String errorCode, Throwable t) {
    super(INVALID_REQUEST_ERROR, errorCode, HttpStatus.NOT_FOUND, t);
  }
}
