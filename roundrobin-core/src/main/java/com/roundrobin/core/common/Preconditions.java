package com.roundrobin.core.common;

import com.roundrobin.core.exception.AbstractException;
import com.roundrobin.core.exception.BadRequestException;

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

}
