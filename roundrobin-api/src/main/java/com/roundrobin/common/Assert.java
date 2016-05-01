package com.roundrobin.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.roundrobin.exception.ClientException;

@Component
@Scope("singleton")
public class Assert {
  
  public static void isTrue(boolean expression, ErrorCode errorCode) {
    if (!expression) {
      throw new ClientException(errorCode, errorCode+"");
    }
  }
}
