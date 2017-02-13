package com.roundrobin.core.exception;

import org.springframework.http.HttpStatus;

import static com.roundrobin.core.common.ErrorTypes.SERVICE_ERROR;

public class ServiceException extends AbstractException {

  private static final long serialVersionUID = 8146716232001950974L;

  public ServiceException(String errorCode) {
    super(SERVICE_ERROR, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ServiceException(String errorCode, Throwable t) {
    super(SERVICE_ERROR, errorCode, HttpStatus.INTERNAL_SERVER_ERROR, t);
  }
}
