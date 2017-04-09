package com.roundrobin.auth.web;

import com.roundrobin.auth.api.UserActionTo;
import com.roundrobin.auth.api.UserTo;
import com.roundrobin.auth.groups.CreateUserValidator;
import com.roundrobin.auth.groups.RequestUserActionValidator;
import com.roundrobin.auth.groups.ResetPasswordValidator;
import com.roundrobin.auth.groups.VerifyValidator;
import com.roundrobin.auth.service.UserActionService;
import com.roundrobin.auth.service.UserService;
import com.roundrobin.core.api.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user-action", produces = {"application/json"}, consumes = {"application/json"})
public class UserActionResource {

  @Autowired
  private UserService userService;

  @Autowired
  private UserActionService userActionService;


  @RequestMapping(value = "create-user", method = RequestMethod.POST)
  public Response<Boolean> create(@RequestBody @Validated(CreateUserValidator.class) UserTo userTo) {
    userService.create(userTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "request-verify", method = RequestMethod.POST)
  public Response<Boolean> requestVerify(
          @RequestBody @Validated(RequestUserActionValidator.class) UserActionTo userActionTo) {
    userActionService.requestVerify(userActionTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "verify", method = RequestMethod.POST)
  public Response<Boolean> verify(@RequestBody @Validated(VerifyValidator.class) UserActionTo userActionTo) {
    userActionService.verifyUser(userActionTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "request-reset-password", method = RequestMethod.POST)
  public Response<Boolean> requestResetPassword(
          @RequestBody @Validated(RequestUserActionValidator.class) UserActionTo userActionTo) {
    userActionService.requestResetPassword(userActionTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "reset-password", method = RequestMethod.POST)
  public Response<Boolean> resetPassword(
          @RequestBody @Validated(ResetPasswordValidator.class) UserActionTo userActionTo) {
    userActionService.resetPassword(userActionTo);
    return new Response<>(true);
  }
}
