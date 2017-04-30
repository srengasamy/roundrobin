package com.roundrobin.vault.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.User;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.groups.CreateProfileValidator;
import com.roundrobin.vault.groups.UpdateProfileValidator;
import com.roundrobin.vault.service.UserProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user-profile", produces = {"application/json"})
public class UserProfileResource {
  @Autowired
  private UserProfileService service;

  @RequestMapping(method = RequestMethod.GET)
  public Response<UserProfileTo> read(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.read(user));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.POST)
  public Response<UserProfileTo> create(
          @RequestBody @Validated(CreateProfileValidator.class) UserProfileTo userProfileTo,
          Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.create(user, userProfileTo));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<UserProfileTo> update(
          @RequestBody @Validated(UpdateProfileValidator.class) UserProfileTo userProfileTo,
          Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.update(user, userProfileTo));
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public Response<Boolean> delete(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    service.delete(user);
    return new Response<>(true);
  }
}
