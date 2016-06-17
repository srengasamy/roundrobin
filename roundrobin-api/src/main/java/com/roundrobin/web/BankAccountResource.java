package com.roundrobin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roundrobin.api.BankAccountTo;
import com.roundrobin.api.Response;
import com.roundrobin.groups.CreateBankAccountValidator;
import com.roundrobin.groups.UpdateBankAccountValidator;
import com.roundrobin.services.BankAccountService;

import java.util.List;

@RestController
@RequestMapping(value = "bank-account", produces = {"application/json"})
public class BankAccountResource {
  @Autowired
  private BankAccountService service;

  @RequestMapping(value = "{bankAccountId}", method = RequestMethod.GET)
  public Response<BankAccountTo> read(@PathVariable String bankAccountId) {
    return new Response<>(service.read(bankAccountId));
  }

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Response<BankAccountTo> create(
          @RequestBody @Validated(CreateBankAccountValidator.class) BankAccountTo bankAccountTo) {
    return new Response<>(service.create(bankAccountTo));
  }

  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<BankAccountTo> update(
          @RequestBody @Validated(UpdateBankAccountValidator.class) BankAccountTo bankAccountTo) {
    return new Response<>(service.update(bankAccountTo));
  }

  @RequestMapping(value = "{bankAccountId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String bankAccountId) {
    service.delete(bankAccountId);
    return new Response<>(true);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Response<List<BankAccountTo>> list(@RequestParam("profileId") String profileId) {
    return new Response<>(service.list(profileId));
  }
}
