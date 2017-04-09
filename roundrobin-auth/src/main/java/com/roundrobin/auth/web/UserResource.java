package com.roundrobin.auth.web;

import com.roundrobin.auth.api.UserTo;
import com.roundrobin.auth.service.UserService;
import com.roundrobin.core.api.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "admin", produces = {"application/json"}, consumes = {"application/json"})
public class UserResource {
  @Autowired
  private UserService service;

  @RequestMapping(value = "user", method = RequestMethod.GET)
  public Response<UserTo> user(Authentication authentication) {
    String userId = (String) authentication.getPrincipal();
    return new Response<>(service.read(userId));
  }

  @RequestMapping(value = "user", method = RequestMethod.DELETE)
  public Response<Boolean> delete(Authentication authentication) {
    String userId = (String) authentication.getPrincipal();
    service.delete(userId);
    return new Response<>(true);
  }

}
