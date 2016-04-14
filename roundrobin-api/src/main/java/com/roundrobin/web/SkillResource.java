package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.domain.Skill;
import com.roundrobin.groups.UpdateSkillValidator;
import com.roundrobin.services.SkillService;

@RestController
@RequestMapping(value = "skill", produces = {"application/json"})
public class SkillResource {
  @Autowired
  private SkillService service;

  @RequestMapping(value = "{skillId}", method = RequestMethod.GET)
  public Response<Skill> read(@PathVariable String skillId) {
    return new Response<>(service.read(skillId));
  }

  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<String> update(@RequestBody @Validated(UpdateSkillValidator.class) Skill skill) {
    return new Response<>(service.update(skill).getId());
  }
}
