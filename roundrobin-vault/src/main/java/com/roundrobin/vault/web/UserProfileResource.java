package com.roundrobin.vault.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.common.User;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.groups.CreateProfileValidator;
import com.roundrobin.vault.groups.UpdateProfileValidator;
import com.roundrobin.vault.services.UserProfileService;

@RestController
@RequestMapping(value = "user-profile", produces = {"application/json"})
public class UserProfileResource {
  @Autowired
  private UserProfileService service;

  @RequestMapping(method = RequestMethod.GET)
  public Response<UserProfileTo> read(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.read(user.getUserId()));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.POST)
  public Response<UserProfileTo> create(
      @RequestBody @Validated(CreateProfileValidator.class) UserProfileTo userProfileTo,
      Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    userProfileTo.setUserId(user.getUserId());
    return new Response<>(service.create(userProfileTo));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<UserProfileTo> update(
      @RequestBody @Validated(UpdateProfileValidator.class) UserProfileTo userProfileTo,
      Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    userProfileTo.setUserId(user.getUserId());
    return new Response<>(service.update(userProfileTo));
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public Response<Boolean> delete(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    service.delete(user.getUserId());
    return new Response<>(true);
  }
}
