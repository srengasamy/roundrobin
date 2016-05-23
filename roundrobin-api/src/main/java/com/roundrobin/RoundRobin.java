package com.roundrobin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO Remove logs file from git

@SpringBootApplication
public class RoundRobin {
  public static void main(String[] args) {
    SpringApplication.run(RoundRobin.class, args);
  }
}
