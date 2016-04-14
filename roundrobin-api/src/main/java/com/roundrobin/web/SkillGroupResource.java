package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.domain.SkillGroup;
import com.roundrobin.groups.CreateSkillGroupValidator;
import com.roundrobin.groups.UpdateSkillGroupValidator;
import com.roundrobin.services.SkillGroupService;

@RestController
@RequestMapping(value = "skill-group", produces = {"application/json"})
public class SkillGroupResource {
  @Autowired
  private SkillGroupService service;

  @RequestMapping(value = "{skillGroupId}", method = RequestMethod.GET)
  public Response<SkillGroup> read(@PathVariable String skillGroupId) {
    return new Response<>(service.read(skillGroupId));
  }

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Response<String> create(@RequestBody @Validated(CreateSkillGroupValidator.class) SkillGroup skillGroup) {
    return new Response<>(service.create(skillGroup).getId());
  }

  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<String> update(@RequestBody @Validated(UpdateSkillGroupValidator.class) SkillGroup skillGroup) {
    return new Response<>(service.update(skillGroup).getId());
  }
}
