package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.api.UserTo;
import com.roundrobin.groups.UpdateUserValidator;
import com.roundrobin.service.UserService;

@RestController
@RequestMapping(value = "admin", produces = {"application/json"})
public class UserResource {
  @Autowired
  private UserService service;

  @RequestMapping(value = "user", consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<Boolean> update(@RequestBody @Validated(UpdateUserValidator.class) UserTo userTo) {
    service.update(userTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "user/{userId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String userId) {
    service.delete(userId);
    return new Response<>(true);
  }
}
