package com.roundrobin.vault.api;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.vault.groups.CreateCreditCardValidator;
import com.roundrobin.vault.groups.CreditCardValidator;
import com.roundrobin.vault.groups.UpdateCreditCardValidator;

@JsonInclude(Include.NON_ABSENT)
public class CreditCardTo {
  @NotBlank(groups = UpdateCreditCardValidator.class)
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateCreditCardValidator.class)
  @CreditCardNumber(groups = CreateCreditCardValidator.class)
  private Optional<String> cardNumber = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateCreditCardValidator.class)
  @Range(min = 1, max = 12, groups = CreditCardValidator.class)
  private Optional<Byte> expiryMonth = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateCreditCardValidator.class)
  @Range(min = 16, max = 99, groups = CreditCardValidator.class)
  private Optional<Short> expiryYear = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreditCardValidator.class)
  @Length(max = 5, groups = CreditCardValidator.class)
  private Optional<String> cvv = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateCreditCardValidator.class)
  @Length(max = 7, groups = CreditCardValidator.class)
  private Optional<String> postalCode = Optional.empty();

  private Optional<String> userId = Optional.empty();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Optional<String> getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(Optional<String> cardNumber) {
    this.cardNumber = cardNumber;
  }

  public Optional<Byte> getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(Optional<Byte> expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public Optional<Short> getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(Optional<Short> expiryYear) {
    this.expiryYear = expiryYear;
  }

  public Optional<String> getCvv() {
    return cvv;
  }

  public void setCvv(Optional<String> cvv) {
    this.cvv = cvv;
  }

  public Optional<String> getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(Optional<String> postalCode) {
    this.postalCode = postalCode;
  }

  public Optional<String> getUserId() {
    return userId;
  }

  public void setUserId(Optional<String> userId) {
    this.userId = userId;
  }


}
