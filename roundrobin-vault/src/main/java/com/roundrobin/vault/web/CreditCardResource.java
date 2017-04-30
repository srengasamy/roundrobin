package com.roundrobin.vault.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.User;
import com.roundrobin.vault.api.CreditCardTo;
import com.roundrobin.vault.groups.CreateCreditCardValidator;
import com.roundrobin.vault.groups.UpdateCreditCardValidator;
import com.roundrobin.vault.service.CreditCardService;


@RestController
@RequestMapping(value = "credit-card", produces = {"application/json"})
public class CreditCardResource {
  @Autowired
  private CreditCardService service;

  @RequestMapping(value = "{creditCardId}", method = RequestMethod.GET)
  public Response<CreditCardTo> read(@PathVariable String creditCardId, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.read(user, creditCardId));
  }

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Response<CreditCardTo> create(
      @RequestBody @Validated(CreateCreditCardValidator.class) CreditCardTo creditCardTo,
      Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.create(user, creditCardTo));
  }

  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<CreditCardTo> update(
      @RequestBody @Validated(UpdateCreditCardValidator.class) CreditCardTo creditCardTo,
      Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.update(user, creditCardTo));
  }

  @RequestMapping(value = "{creditCardId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String creditCardId, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    service.delete(user, creditCardId);
    return new Response<>(true);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Response<List<CreditCardTo>> list(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.list(user));
  }
}
