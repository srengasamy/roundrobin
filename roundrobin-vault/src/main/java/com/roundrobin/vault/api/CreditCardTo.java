package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.vault.groups.CreateCreditCardValidator;
import com.roundrobin.vault.groups.CreditCardValidator;
import com.roundrobin.vault.groups.UpdateCreditCardValidator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@JsonInclude(Include.NON_ABSENT)
public class CreditCardTo {
  @NotBlank(groups = UpdateCreditCardValidator.class)
  private String id;

  @JsonProperty("last4")
  @Length(max = 4, groups = CreditCardValidator.class)
  @NotBlank(groups = CreateCreditCardValidator.class)
  private String last4;

  @JsonProperty("expiry_month")
  @NotNull(groups = CreateCreditCardValidator.class)
  @Range(min = 1, max = 12, groups = CreditCardValidator.class)
  private Byte expiryMonth;

  @JsonProperty("expiry_year")
  @NotNull(groups = CreateCreditCardValidator.class)
  @Range(min = 2017, max = 2099, groups = CreditCardValidator.class)
  private Short expiryYear;

  @NotBlank(groups = CreateCreditCardValidator.class)
  @Length(max = 25, groups = CreditCardValidator.class)
  private String brand;

  @JsonProperty("postal_code")
  @NotBlank(groups = CreateCreditCardValidator.class)
  @Length(max = 7, groups = CreditCardValidator.class)
  private String postalCode;

}
