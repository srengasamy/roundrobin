package com.roundrobin.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "credit_card")
public class CreditCard {
  @Id
  private String id;

  private String nameOnCard;
  private String cardNumber;
  private Byte expiryMonth;
  private Short expiryYear;
  private String cvv;
  private String postalCode;
  private Boolean active;
  private Boolean valid;
  private DateTime created;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNameOnCard() {
    return nameOnCard;
  }

  public void setNameOnCard(String nameOnCard) {
    this.nameOnCard = nameOnCard;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public Byte getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(Byte expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public Short getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(Short expiryYear) {
    this.expiryYear = expiryYear;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
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

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

}
