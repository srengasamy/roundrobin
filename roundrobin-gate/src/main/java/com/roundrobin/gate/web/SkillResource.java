package com.roundrobin.gate.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.User;
import com.roundrobin.gate.api.SkillTo;
import com.roundrobin.gate.groups.CreateSkillValidator;
import com.roundrobin.gate.groups.UpdateSkillValidator;
import com.roundrobin.gate.service.SkillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "skill", produces = {"application/json"})
public class SkillResource {
  @Autowired
  private SkillService service;

  @RequestMapping(value = "{skillId}", method = RequestMethod.GET)
  public Response<SkillTo> read(@PathVariable String skillId, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.read(user, skillId));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.POST)
  public Response<SkillTo> create(@RequestBody @Validated(CreateSkillValidator.class) SkillTo skillTo,
      Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.create(user, skillTo));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<SkillTo> update(@RequestBody @Validated(UpdateSkillValidator.class) SkillTo skillTo,
      Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.update(user, skillTo));
  }

  @RequestMapping(value = "{skillId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String skillId, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    service.delete(user, skillId);
    return new Response<>(true);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Response<List<SkillTo>> list(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.list(user));
  }
}
