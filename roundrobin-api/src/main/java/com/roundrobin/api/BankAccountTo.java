package com.roundrobin.api;

import java.util.Optional;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.groups.BankAccountValidator;
import com.roundrobin.groups.CreateBankAccountValidator;
import com.roundrobin.groups.UpdateBankAccountValidator;

@JsonInclude(Include.NON_ABSENT)
public class BankAccountTo {
  @NotBlank(groups = UpdateBankAccountValidator.class)
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateBankAccountValidator.class)
  private Optional<String> bankName = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateBankAccountValidator.class)
  private Optional<String> nameOnAccount = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateBankAccountValidator.class)
  private Optional<String> accountNumber = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateBankAccountValidator.class)
  private Optional<String> routingNumber = Optional.empty();

  private Optional<String> description = Optional.empty();

  @NotBlank(groups = BankAccountValidator.class)
  private String userProfileId;

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

  public String getUserProfileId() {
    return userProfileId;
  }

  public void setUserProfileId(String userProfileId) {
    this.userProfileId = userProfileId;
  }

}
