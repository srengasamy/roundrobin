package com.roundrobin.vault.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "bank_account")
public class BankAccount {
  @Id
  private String id;
  private String bankName;
  private String last4;
  private boolean active;
  private DateTime created;

  @DBRef
  private UserProfile profile;
}
