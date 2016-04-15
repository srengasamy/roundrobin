package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.api.UserProfileTo;
import com.roundrobin.groups.CreateProfileValidator;
import com.roundrobin.groups.UpdateProfileValidator;
import com.roundrobin.services.UserProfileService;

@RestController
@RequestMapping(value = "user-profile", produces = {"application/json"})
public class UserProfileResource {
  @Autowired
  private UserProfileService service;

  @RequestMapping(value = "{userProfileId}", method = RequestMethod.GET)
  public Response<UserProfileTo> read(@PathVariable String userProfileId) {
    return new Response<>(service.read(userProfileId));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.POST)
  public Response<UserProfileTo> create(
      @RequestBody @Validated(CreateProfileValidator.class) UserProfileTo userProfileTo) {
    return new Response<>(service.create(userProfileTo));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<UserProfileTo> update(
      @RequestBody @Validated(UpdateProfileValidator.class) UserProfileTo userProfileTo) {
    return new Response<>(service.update(userProfileTo));
  }

  @RequestMapping(value = "{userProfileId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String userProfileId) {
    service.delete(userProfileId);
    return new Response<>(true);
  }
}
