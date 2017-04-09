package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.vault.groups.BankAccountValidator;
import com.roundrobin.vault.groups.CreateBankAccountValidator;
import com.roundrobin.vault.groups.UpdateBankAccountValidator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import java.util.Optional;

import lombok.Data;

@Data
@JsonInclude(Include.NON_ABSENT)
public class BankAccountTo {
  @NotBlank(groups = UpdateBankAccountValidator.class)
  private String id;

  @UnwrapValidatedValue
  @JsonProperty("bank_name")
  @NotBlank(groups = CreateBankAccountValidator.class)
  @Length(max = 50, groups = BankAccountValidator.class)
  private Optional<String> bankName = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("name_on_account")
  @NotBlank(groups = CreateBankAccountValidator.class)
  @Length(max = 100, groups = BankAccountValidator.class)
  private Optional<String> nameOnAccount = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("account_number")
  @NotBlank(groups = CreateBankAccountValidator.class)
  @Length(max = 35, groups = BankAccountValidator.class)
  private Optional<String> accountNumber = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("routing_number")
  @NotBlank(groups = BankAccountValidator.class)
  @Length(max = 35, groups = BankAccountValidator.class)
  private Optional<String> routingNumber = Optional.empty();

  @UnwrapValidatedValue
  @Length(max = 50, groups = BankAccountValidator.class)
  private Optional<String> description = Optional.empty();

}
