package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.CreditCardTo;
import com.roundrobin.api.Response;
import com.roundrobin.groups.CreateCreditCardValidator;
import com.roundrobin.groups.UpdateCreditCardValidator;
import com.roundrobin.services.CreditCardService;

@RestController
@RequestMapping(value = "credit-card", produces = {"application/json"})
public class CreditCardResource {
  @Autowired
  private CreditCardService service;

  @RequestMapping(value = "{creditCardId}", method = RequestMethod.GET)
  public Response<CreditCardTo> read(@PathVariable String creditCardId) {
    return new Response<>(service.read(creditCardId));
  }

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Response<CreditCardTo> create(
      @RequestBody @Validated(CreateCreditCardValidator.class) CreditCardTo creditCardTo) {
    return new Response<>(service.create(creditCardTo));
  }
  
  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<CreditCardTo> update(
      @RequestBody @Validated(UpdateCreditCardValidator.class) CreditCardTo creditCardTo) {
    return new Response<>(service.update(creditCardTo));
  }
  
  @RequestMapping(value = "{creditCardId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String creditCardId) {
    service.delete(creditCardId);
    return new Response<>(true);
  }
}
