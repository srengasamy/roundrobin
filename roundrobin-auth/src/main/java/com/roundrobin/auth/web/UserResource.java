package com.roundrobin.auth.web;

import com.roundrobin.auth.api.UserTo;
import com.roundrobin.auth.service.UserService;
import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "admin", produces = {"application/json"}, consumes = {"application/json"})
public class UserResource {
  @Autowired
  private UserService service;

  @RequestMapping(value = "user", method = RequestMethod.GET)
  public Response<UserInfo> user(Authentication authentication) {
    String userId = (String) authentication.getPrincipal();
    UserTo userTo = service.read(userId);
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId(userTo.getUserId());
    userInfo.setUsername(userTo.getUsername());
    userInfo.setRoles(userTo.getRoles());
    userInfo.setVerified(userTo.isVerified());
    return new Response<>(userInfo);
  }

  @RequestMapping(value = "user", method = RequestMethod.DELETE)
  public Response<Boolean> delete(Authentication authentication) {
    String userId = (String) authentication.getPrincipal();
    service.delete(userId);
    return new Response<>(true);
  }

}
