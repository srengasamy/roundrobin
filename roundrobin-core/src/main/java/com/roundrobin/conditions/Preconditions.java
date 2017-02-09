package com.roundrobin.conditions;

import com.roundrobin.exceptions.AbstractException;
import com.roundrobin.exceptions.BadRequestException;

public class Preconditions {
  public static void checkArgument(boolean expression, AbstractException e) {
    if (!expression) {
      throw e;
    }
  }

  public static void badRequest(boolean expression, String errorCode) {
    if (!expression) {
      throw new BadRequestException(errorCode);
    }
  }

  public static void badRequest(boolean expression, String errorCode, String param) {
    if (!expression) {
      throw new BadRequestException(errorCode, param);
    }
  }

  public static void badRequest(boolean expression, String errorCode, Throwable t) {
    if (!expression) {
      throw new BadRequestException(errorCode, t);
    }
  }
}
