package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.api.UserActionTo;
import com.roundrobin.groups.UserActivationValidator;
import com.roundrobin.services.UserActionService;

@RestController
@RequestMapping(value = "user-action", produces = {"application/json"})
public class UserActionResource {
  @Autowired
  private UserActionService service;

  @RequestMapping(value = "activate", method = RequestMethod.GET)
  public Response<String> read(@Validated(UserActivationValidator.class) UserActionTo userActionTo) {
    service.activate(userActionTo);
    return new Response<>("Success");
  }
}
