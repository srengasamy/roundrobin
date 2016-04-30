package com.roundrobin.services;

import com.roundrobin.api.BankAccountTo;
import com.roundrobin.domain.BankAccount;

public interface BankAccountService {
  public BankAccount get(String id);
  public BankAccountTo read(String id);
  public BankAccountTo create(BankAccountTo bankAccountTo);
  public BankAccountTo update(BankAccountTo bankAccountTo);
  public void delete(String id);
  public BankAccount save(BankAccount bankAccount);
}
