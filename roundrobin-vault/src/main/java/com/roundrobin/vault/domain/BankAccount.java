package com.roundrobin.vault.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bank_account")
public class BankAccount {
  @Id
  private String id;
  private String bankName;
  private String nameOnAccount;
  private String accountNumber;
  private String maskedAccountNumber;
  private String routingNumber;
  private String description;
  private Boolean active;
  private Boolean valid;
  private DateTime created;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getNameOnAccount() {
    return nameOnAccount;
  }

  public void setNameOnAccount(String nameOnAccount) {
    this.nameOnAccount = nameOnAccount;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getRoutingNumber() {
    return routingNumber;
  }

  public void setRoutingNumber(String routingNumber) {
    this.routingNumber = routingNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Boolean getValid() {
    return valid;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public void setMaskedAccountNumber(String maskedAccountNumber) {
    this.maskedAccountNumber = maskedAccountNumber;
  }

  public String getMaskedAccountNumber() {

    return maskedAccountNumber;
  }

}
