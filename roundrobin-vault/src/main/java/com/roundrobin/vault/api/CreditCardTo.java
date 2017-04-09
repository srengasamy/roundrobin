package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.vault.groups.CreateCreditCardValidator;
import com.roundrobin.vault.groups.CreditCardValidator;
import com.roundrobin.vault.groups.UpdateCreditCardValidator;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@JsonInclude(Include.NON_ABSENT)
public class CreditCardTo {
  @NotBlank(groups = UpdateCreditCardValidator.class)
  private String id;

  @UnwrapValidatedValue
  @JsonProperty("card_number")
  @NotBlank(groups = CreateCreditCardValidator.class)
  @CreditCardNumber(groups = CreateCreditCardValidator.class)
  private Optional<String> cardNumber = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("expiry_month")
  @NotNull(groups = CreateCreditCardValidator.class)
  @Range(min = 1, max = 12, groups = CreditCardValidator.class)
  private Optional<Byte> expiryMonth = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("expiry_year")
  @NotNull(groups = CreateCreditCardValidator.class)
  @Range(min = 16, max = 99, groups = CreditCardValidator.class)
  private Optional<Short> expiryYear = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreditCardValidator.class)
  @Length(max = 5, groups = CreditCardValidator.class)
  private Optional<String> cvv = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("postal_code")
  @NotBlank(groups = CreateCreditCardValidator.class)
  @Length(max = 7, groups = CreditCardValidator.class)
  private Optional<String> postalCode = Optional.empty();

}
