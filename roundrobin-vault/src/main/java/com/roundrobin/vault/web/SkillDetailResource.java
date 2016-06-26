package com.roundrobin.vault.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.Response;
import com.roundrobin.vault.api.SkillDetailTo;
import com.roundrobin.vault.groups.CreateSkillDetailValidator;
import com.roundrobin.vault.groups.UpdateSkillDetailValidator;
import com.roundrobin.vault.services.SkillDetailService;

@RestController
@RequestMapping(value = "skill-detail", produces = {"application/json"})
public class SkillDetailResource {
  @Autowired
  private SkillDetailService service;

  @RequestMapping(value = "{skillDetailId}", method = RequestMethod.GET)
  public Response<SkillDetailTo> read(@PathVariable String skillDetailId) {
    return new Response<>(service.read(skillDetailId));
  }

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Response<SkillDetailTo> create(
          @RequestBody @Validated(CreateSkillDetailValidator.class) SkillDetailTo skillDetailTo) {
    return new Response<>(service.create(skillDetailTo));
  }

  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<SkillDetailTo> update(
          @RequestBody @Validated(UpdateSkillDetailValidator.class) SkillDetailTo skillDetailTo) {
    return new Response<>(service.update(skillDetailTo));
  }

  @RequestMapping(value = "{skillDetailId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String skillDetailId) {
    service.delete(skillDetailId);
    return new Response<>(true);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Response<List<SkillDetailTo>> list() {
    return new Response<>(service.list());
  }
}
