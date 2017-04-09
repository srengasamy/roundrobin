package com.roundrobin.vault.domain;


import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "credit_card")
public class CreditCard {
  @Id
  private String id;

  private String cardNumber;
  private String maskedCardNumber;
  private Byte expiryMonth;
  private Short expiryYear;
  private String cvv;
  private String postalCode;
  private Boolean active;
  private Boolean valid;
  private DateTime created;

}
