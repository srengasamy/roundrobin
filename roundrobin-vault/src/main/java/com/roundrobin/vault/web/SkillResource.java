package com.roundrobin.vault.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.User;
import com.roundrobin.vault.api.SkillTo;
import com.roundrobin.vault.groups.CreateSkillValidator;
import com.roundrobin.vault.groups.UpdateSkillValidator;
import com.roundrobin.vault.service.SkillService;


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
