package com.roundrobin.web;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.api.UserTo;
import com.roundrobin.groups.UpdateUserValidator;
import com.roundrobin.security.CustomUserDetails;
import com.roundrobin.service.UserService;

@RestController
@RequestMapping(value = "admin", produces = {"application/json"})
public class UserResource {
  @Autowired
  private UserService service;

  @RequestMapping(value = "user", method = RequestMethod.GET)
  public Response<UserTo> user(Authentication authentication) {
    UserTo userTo = new UserTo();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    userTo.setRoles(userDetails.getAuthorities().stream().map(c -> c.getAuthority()).collect(Collectors.toList()));
    userTo.setUserId(userDetails.getUserId());
    return new Response<>(userTo);
  }

  @RequestMapping(value = "user", consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<Boolean> update(@RequestBody @Validated(UpdateUserValidator.class) UserTo userTo,
      Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    userTo.setUserId(userDetails.getUserId());
    service.update(userTo);
    return new Response<>(true);
  }

  @RequestMapping(value = "user", method = RequestMethod.DELETE)
  public Response<Boolean> delete(Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    service.delete(userDetails.getUserId());
    return new Response<>(true);
  }

}
