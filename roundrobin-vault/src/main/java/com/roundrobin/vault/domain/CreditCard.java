package com.roundrobin.vault.domain;


import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "credit_card")
public class CreditCard {
  @Id
  private String id;

  private String last4;
  private String brand;
  private String postalCode;
  private Byte expiryMonth;
  private Short expiryYear;
  private boolean active;
  private DateTime created;

  @DBRef
  private UserProfile profile;
}
