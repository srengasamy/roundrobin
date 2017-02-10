package com.roundrobin.auth;

import com.roundrobin.core.common.ErrorCodes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Created by rengasu on 2/9/17.
 */
@SpringBootApplication
public class RoundRobin {
  public static void main(String[] args) {
    ErrorCodes e;
    SpringApplication.run(RoundRobin.class, args);
  }
}
