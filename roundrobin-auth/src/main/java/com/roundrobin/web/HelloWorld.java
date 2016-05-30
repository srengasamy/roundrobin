package com.roundrobin.web;

import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rengasu on 5/27/16.
 */
@RestController
@RequestMapping(value = "hello", produces = {"application/json"})
public class HelloWorld {

  @RequestMapping("/")
  public String home() {
    return "Hello World";
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public String create(@RequestBody MultiValueMap<String, String> map) {
    return "OK";
  }
}
