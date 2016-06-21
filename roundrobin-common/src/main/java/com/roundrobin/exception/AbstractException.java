package com.roundrobin.exception;

public abstract class AbstractException extends RuntimeException {

  private static final long serialVersionUID = -2350337680024841058L;

  public AbstractException(String message) {
    super(message);
  }

  public AbstractException(String message, Throwable t) {
    super(message, t);
  }

  public abstract int getCode();
}
