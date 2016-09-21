package com.roundrobin.exception;

import static com.roundrobin.error.ErrorType.AUTHENTICATION_ERROR;

import org.springframework.http.HttpStatus;

public class AuthException extends AbstractException {
  private static final long serialVersionUID = -6039187233464180033L;

  public AuthException(String errorCode) {
    super(AUTHENTICATION_ERROR, errorCode, HttpStatus.UNAUTHORIZED);
  }

  public AuthException(String errorCode, Throwable t) {
    super(AUTHENTICATION_ERROR, errorCode, HttpStatus.UNAUTHORIZED, t);
  }
}
