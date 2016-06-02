package com.roundrobin.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:roundrobin-common.properties")
public class ClientMessages {
  @Autowired
  Environment env;

  public String getErrorMessage(ErrorCode errorCode) {
    return env.getProperty(errorCode.toString());
  }
}
