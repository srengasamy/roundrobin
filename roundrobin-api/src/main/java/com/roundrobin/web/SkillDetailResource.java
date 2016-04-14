package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.domain.SkillDetail;
import com.roundrobin.groups.CreateSkillDetailValidator;
import com.roundrobin.groups.UpdateSkillDetailValidator;
import com.roundrobin.services.SkillDetailService;

@RestController
@RequestMapping(value = "skill-detail", produces = {"application/json"})
public class SkillDetailResource {
  @Autowired
  private SkillDetailService service;

  @RequestMapping(value = "{skillDetailId}", method = RequestMethod.GET)
  public Response<SkillDetail> read(@PathVariable String skillDetailId) {
    return new Response<>(service.read(skillDetailId));
  }

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Response<String> create(@RequestBody @Validated(CreateSkillDetailValidator.class) SkillDetail skillDetail) {
    return new Response<>(service.create(skillDetail).getId());
  }

  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<String> update(@RequestBody @Validated(UpdateSkillDetailValidator.class) SkillDetail skillDetail) {
    return new Response<>(service.update(skillDetail).getId());
  }
}
