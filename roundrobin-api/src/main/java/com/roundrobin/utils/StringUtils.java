package com.roundrobin.utils;


/**
 * Created by rengasu on 5/21/16.
 */
public class StringUtils {
  private static int DEFAULT = 4;

  public static String mask(String value) {
    return org.apache.commons.lang3.StringUtils.repeat('*', value.length() - DEFAULT) + value.substring(value.length() - 4);
  }
}
