package com.roundrobin.map.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.api.User;
import com.roundrobin.map.api.TrailTo;
import com.roundrobin.map.groups.TrailValidator;
import com.roundrobin.map.service.TrailService;

@RestController
@RequestMapping(value = "trail", produces = {"application/json"})
public class TrailResource {

  @Autowired
  private TrailService service;

  @RequestMapping(method = RequestMethod.POST)
  public Response<TrailTo> create(Authentication authentication, @RequestBody @Validated(TrailValidator.class) TrailTo trailTo) {
    User user = (User) authentication.getPrincipal();
    trailTo.setVendorId(user.getUserId());
    return new Response<>(service.create(trailTo));
  }
}
