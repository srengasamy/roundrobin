package com.roundrobin.vault.api;

import java.util.Optional;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.vault.groups.BankAccountValidator;
import com.roundrobin.vault.groups.CreateBankAccountValidator;
import com.roundrobin.vault.groups.UpdateBankAccountValidator;

@JsonInclude(Include.NON_ABSENT)
public class BankAccountTo {
  @NotBlank(groups = UpdateBankAccountValidator.class)
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateBankAccountValidator.class)
  @Length(max = 50, groups = BankAccountValidator.class)
  private Optional<String> bankName = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateBankAccountValidator.class)
  @Length(max = 100, groups = BankAccountValidator.class)
  private Optional<String> nameOnAccount = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateBankAccountValidator.class)
  @Length(max = 35, groups = BankAccountValidator.class)
  private Optional<String> accountNumber = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = BankAccountValidator.class)
  @Length(max = 35, groups = BankAccountValidator.class)
  private Optional<String> routingNumber = Optional.empty();

  @UnwrapValidatedValue
  @Length(max = 50, groups = BankAccountValidator.class)
  private Optional<String> description = Optional.empty();

  private Optional<String> userId = Optional.empty();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Optional<String> getBankName() {
    return bankName;
  }

  public void setBankName(Optional<String> bankName) {
    this.bankName = bankName;
  }

  public Optional<String> getNameOnAccount() {
    return nameOnAccount;
  }

  public void setNameOnAccount(Optional<String> nameOnAccount) {
    this.nameOnAccount = nameOnAccount;
  }

  public Optional<String> getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(Optional<String> accountNumber) {
    this.accountNumber = accountNumber;
  }

  public Optional<String> getRoutingNumber() {
    return routingNumber;
  }

  public void setRoutingNumber(Optional<String> routingNumber) {
    this.routingNumber = routingNumber;
  }

  public Optional<String> getDescription() {
    return description;
  }

  public void setDescription(Optional<String> description) {
    this.description = description;
  }

  public Optional<String> getUserId() {
    return userId;
  }

  public void setUserId(Optional<String> userId) {
    this.userId = userId;
  }


}
