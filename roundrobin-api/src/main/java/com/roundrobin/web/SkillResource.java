package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.api.SkillTo;
import com.roundrobin.groups.CreateSkillValidator;
import com.roundrobin.groups.UpdateSkillValidator;
import com.roundrobin.services.SkillService;

@RestController
@RequestMapping(value = "skill", produces = {"application/json"})
public class SkillResource {
  @Autowired
  private SkillService service;

  @RequestMapping(value = "{skillId}", method = RequestMethod.GET)
  public Response<SkillTo> read(@PathVariable String skillId) {
    return new Response<>(service.read(skillId));
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
    service.delete(skillId);
    return new Response<>(true);
  }
}
