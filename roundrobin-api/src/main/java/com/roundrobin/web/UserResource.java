package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.domain.User;
import com.roundrobin.groups.CreateValidator;
import com.roundrobin.services.UserService;

@RestController
@RequestMapping(value = "user", produces = {"application/json"})
public class UserResource {
  @Autowired
  private UserService service;

  @RequestMapping(value = "{userId}", method = RequestMethod.GET)
  public Response<User> read(@PathVariable String userId) {
    return new Response<>(service.read(userId));
  }

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Response<String> create(@RequestBody @Validated(CreateValidator.class) User user) {
    return new Response<>(service.create(user).getId());
  }


}
