package com.roundrobin.common;

import com.roundrobin.exception.ClientException;

public class Assert {
  public static void isTrue(boolean expression, ErrorCode errorCode) {
    if (!expression) {
      throw new ClientException(errorCode);
    }
  }
}
