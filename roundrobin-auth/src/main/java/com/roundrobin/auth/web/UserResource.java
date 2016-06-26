package com.roundrobin.auth.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.auth.api.UserTo;
import com.roundrobin.auth.groups.UpdateUserValidator;
import com.roundrobin.auth.service.UserService;

@RestController
@RequestMapping(value = "admin", produces = {"application/json"})
public class UserResource {
  @Autowired
  private UserService service;

  @RequestMapping(value = "user", method = RequestMethod.GET)
  public Response<UserTo> user(Authentication authentication) {
    String username = (String) authentication.getPrincipal();
    return new Response<>(service.readByUsername(username));
  }

  @RequestMapping(value = "user", consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<Boolean> update(@RequestBody @Validated(UpdateUserValidator.class) UserTo userTo,
      Authentication authentication) {
    String username = (String) authentication.getPrincipal();
    userTo.setUsername(Optional.of(username));
    service.update(userTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "user", method = RequestMethod.DELETE)
  public Response<Boolean> delete(Authentication authentication) {
    String username = (String) authentication.getPrincipal();
    service.delete(username);
    return new Response<>(true);
  }

}
