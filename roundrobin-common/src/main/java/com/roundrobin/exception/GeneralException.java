package com.roundrobin.exception;

public abstract class GeneralException extends RuntimeException {

  private static final long serialVersionUID = -2350337680024841058L;

  public GeneralException(String message) {
    super(message);
  }

  public GeneralException(String message, Throwable t) {
    super(message, t);
  }

  public abstract int getCode();
}
