package com.roundrobin.conditions;

import com.roundrobin.exception.AbstractException;

public class Preconditions {
  public static void checkArgument(boolean expression, AbstractException e) {
    if (!expression) {
      throw e;
    }
  }

}
