package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.api.UserActionTo;
import com.roundrobin.api.UserTo;
import com.roundrobin.groups.ActivationValidator;
import com.roundrobin.groups.CreateUserValidator;
import com.roundrobin.groups.RequestUserActionValidator;
import com.roundrobin.groups.ResetPasswordValidator;
import com.roundrobin.service.UserActionService;
import com.roundrobin.service.UserService;

@RestController
@RequestMapping(value = "user-action", produces = {"application/json"})
public class UserActionResource {
  @Autowired
  private UserActionService service;

  @Autowired
  private UserService userService;

  @RequestMapping(method = RequestMethod.POST)
  public Response<Boolean> create(@RequestBody @Validated(CreateUserValidator.class) UserTo userTo) {
    userService.create(userTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "request-activate", method = RequestMethod.POST)
  public Response<Boolean> requestActivate(
      @RequestBody @Validated(RequestUserActionValidator.class) UserActionTo userActionTo) {
    service.requestActivate(userActionTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "activate", method = RequestMethod.POST)
  public Response<Boolean> activate(@RequestBody @Validated(ActivationValidator.class) UserActionTo userActionTo) {
    service.activateUser(userActionTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "request-reset-password", method = RequestMethod.POST)
  public Response<Boolean> requestResetPassword(
      @RequestBody @Validated(RequestUserActionValidator.class) UserActionTo userActionTo) {
    service.requestResetPassword(userActionTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "reset-password", method = RequestMethod.POST)
  public Response<Boolean> resetPassword(
      @RequestBody @Validated(ResetPasswordValidator.class) UserActionTo userActionTo) {
    service.resetPassword(userActionTo);
    return new Response<>(true);
  }
}
