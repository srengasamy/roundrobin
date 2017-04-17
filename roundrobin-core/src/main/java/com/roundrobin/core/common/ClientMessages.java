package com.roundrobin.core.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Optional;

@Configuration
@PropertySource("classpath:common.properties")
@PropertySource(value = "classpath:messages.properties", ignoreResourceNotFound = true)
public class ClientMessages {
  @Autowired
  Environment env;

  public String getErrorMessage(String errorCode) {
    Optional<String> message = Optional.ofNullable(env.getProperty(errorCode.toString()));
    if (message.isPresent()) {
      return message.get();
    }
    return "Service error occurred";
  }
}
