package com.roundrobin.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.roundrobin.exception.ClientException;

@Component
@Scope("singleton")
public class Assert {

  public static void isTrue(boolean expression, Object errorCode) {
    isTrue(expression, errorCode.toString());
  }

  public static void isTrue(boolean expression, String errorCode) {
    if (!expression) {
      throw new ClientException(errorCode, errorCode);
    }
  }
}
