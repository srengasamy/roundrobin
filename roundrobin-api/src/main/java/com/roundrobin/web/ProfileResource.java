package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.domain.Profile;
import com.roundrobin.groups.UpdateSkillValidator;
import com.roundrobin.services.ProfileService;

@RestController
@RequestMapping(value = "profile", produces = {"application/json"})
public class ProfileResource {
  @Autowired
  private ProfileService service;

  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<String> update(@RequestBody @Validated(UpdateSkillValidator.class) Profile profile) {
    return new Response<>(service.update(profile).getId());
  }
}
