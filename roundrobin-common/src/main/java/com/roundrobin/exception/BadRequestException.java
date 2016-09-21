package com.roundrobin.exception;

import static com.roundrobin.error.ErrorType.INVALID_REQUEST_ERROR;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractException {

  private static final long serialVersionUID = -6225887773316619787L;

  public BadRequestException(String errorCode) {
    super(INVALID_REQUEST_ERROR, errorCode, HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String errorCode, Throwable t) {
    super(INVALID_REQUEST_ERROR, errorCode, HttpStatus.BAD_REQUEST, t);
  }

}
