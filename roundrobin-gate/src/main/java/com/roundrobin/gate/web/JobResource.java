package com.roundrobin.gate.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.User;
import com.roundrobin.gate.api.JobTo;
import com.roundrobin.gate.groups.CreateJobValidator;
import com.roundrobin.gate.groups.UpdateJobValidator;
import com.roundrobin.gate.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rengasu on 5/8/17.
 */
@RestController
@RequestMapping(value = "job", produces = {"application/json"})
public class JobResource {

  @Autowired
  private JobService service;

  @RequestMapping(value = "{jobId}", method = RequestMethod.GET)
  public Response<JobTo> read(@PathVariable String jobId, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.read(user, jobId));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.POST)
  public Response<JobTo> create(@RequestBody @Validated(CreateJobValidator.class) JobTo jobTo,
                                Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.create(user, jobTo));
  }

  @RequestMapping(consumes = {"application/json"}, method = RequestMethod.PUT)
  public Response<JobTo> update(@RequestBody @Validated(UpdateJobValidator.class) JobTo jobTo,
                                Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.update(user, jobTo));
  }

  @RequestMapping(value = "{jobId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String jobId, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    service.delete(user, jobId);
    return new Response<>(true);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Response<List<JobTo>> list(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.list(user));
  }
}
