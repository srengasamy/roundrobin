package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.api.UserActionTo;
import com.roundrobin.groups.RequestResetPasswordValidator;
import com.roundrobin.groups.UserResetPasswordValidator;
import com.roundrobin.groups.UserActivationValidator;
import com.roundrobin.services.UserActionService;

@RestController
@RequestMapping(value = "user-action", produces = {"application/json"})
public class UserActionResource {
  @Autowired
  private UserActionService service;

  @RequestMapping(value = "activate", method = RequestMethod.POST)
  public Response<Boolean> activate(@RequestBody @Validated(UserActivationValidator.class) UserActionTo userActionTo) {
    service.activateUser(userActionTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "request-reset-password", method = RequestMethod.POST)
  public Response<Boolean> requestResetPassword(@RequestBody @Validated(RequestResetPasswordValidator.class) UserActionTo
                                                        userActionTo) {
    service.requestResetPassword(userActionTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "reset-password", method = RequestMethod.POST)
  public Response<Boolean> resetPassword(@RequestBody @Validated(UserResetPasswordValidator.class) UserActionTo
                                                 userActionTo) {
    service.resetPassword(userActionTo);
    return new Response<>(true);
  }
}
