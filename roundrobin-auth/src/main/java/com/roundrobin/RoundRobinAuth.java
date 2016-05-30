package com.roundrobin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created by rengasu on 5/24/16.
 */
@SpringBootApplication
@EnableResourceServer
public class RoundRobinAuth {
  public static void main(String[] args) {
    SpringApplication.run(RoundRobinAuth.class, args);
  }
}
