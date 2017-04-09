package com.roundrobin.vault.web;


import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.User;
import com.roundrobin.vault.api.BankAccountTo;
import com.roundrobin.vault.groups.CreateBankAccountValidator;
import com.roundrobin.vault.groups.UpdateBankAccountValidator;
import com.roundrobin.vault.service.BankAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "bank-account", produces = {"application/json"})
public class BankAccountResource {
  @Autowired
  private BankAccountService service;

  @RequestMapping(value = "{bankAccountId}", method = RequestMethod.GET)
  public Response<BankAccountTo> read(@PathVariable String bankAccountId, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.read(user.getUserId(), bankAccountId));
  }

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Response<BankAccountTo> create(
          @RequestBody @Validated(CreateBankAccountValidator.class) BankAccountTo bankAccountTo,
          Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.create(user.getUserId(), bankAccountTo));
  }

  @RequestMapping(method = RequestMethod.PUT, consumes = {"application/json"})
  public Response<BankAccountTo> update(
          @RequestBody @Validated(UpdateBankAccountValidator.class) BankAccountTo bankAccountTo,
          Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.update(user.getUserId(), bankAccountTo));
  }

  @RequestMapping(value = "{bankAccountId}", method = RequestMethod.DELETE)
  public Response<Boolean> delete(@PathVariable String bankAccountId, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    service.delete(user.getUserId(), bankAccountId);
    return new Response<>(true);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Response<List<BankAccountTo>> list(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new Response<>(service.list(user.getUserId()));
  }
}
