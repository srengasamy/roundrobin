package com.roundrobin.vault.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.vault.api.SkillTo;
import com.roundrobin.vault.groups.CreateSkillValidator;
import com.roundrobin.vault.groups.UpdateSkillValidator;
import com.roundrobin.vault.services.SkillService;

@RestController
@RequestMapping(value = "skill", produces = {"application/json"})
public class SkillResource {
  @Autowired
  private SkillService service;

  @RequestMapping(value = "{skillId}", method = RequestMethod.GET)
  public Response<SkillTo> read(@PathVariable String skillId) {
    return new Response<>(service.read(null, skillId));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.POST)
  public Response<SkillTo> create(@RequestBody @Validated(CreateSkillValidator.class) SkillTo skillTo) {
    return new Response<>(service.create(skillTo));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<SkillTo> update(@RequestBody @Validated(UpdateSkillValidator.class) SkillTo skillTo) {
    return new Response<>(service.update(skillTo));
  }

  @RequestMapping(value = "{skillId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String skillId) {
    service.delete(null, skillId);
    return new Response<>(true);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Response<List<SkillTo>> list(@RequestParam("profileId") String profileId) {
    return new Response<>(service.list(profileId));
  }
}
