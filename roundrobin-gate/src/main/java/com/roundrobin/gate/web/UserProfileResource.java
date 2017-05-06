package com.roundrobin.gate.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.User;
import com.roundrobin.gate.api.UserProfileTo;
import com.roundrobin.gate.groups.CreateProfileValidator;
import com.roundrobin.gate.groups.UpdateProfileValidator;
import com.roundrobin.gate.service.UserProfileService;

import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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

}
