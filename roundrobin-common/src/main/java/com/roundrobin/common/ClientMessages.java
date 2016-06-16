package com.roundrobin.common;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.roundrobin.exception.ServerException;

@Configuration
@PropertySource("classpath:common.properties")
@PropertySource(value = "classpath:messages.properties", ignoreResourceNotFound = true)
public class ClientMessages {
  @Autowired
  Environment env;

  public String getErrorMessage(Integer errorCode) {
    Optional<String> message = Optional.ofNullable(env.getProperty(errorCode.toString()));
    if (message.isPresent()) {
      return message.get();
    }
    throw new ServerException(ErrorCode.MESSAGE_NOT_FOUND);
  }
}
