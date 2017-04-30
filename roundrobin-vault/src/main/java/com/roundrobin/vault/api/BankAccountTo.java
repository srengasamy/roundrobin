package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.vault.groups.BankAccountValidator;
import com.roundrobin.vault.groups.CreateBankAccountValidator;
import com.roundrobin.vault.groups.UpdateBankAccountValidator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
//TODO: NotBlack vs NonEmpty
@Data
@JsonInclude(Include.NON_ABSENT)
public class BankAccountTo {
  @NotBlank(groups = UpdateBankAccountValidator.class)
  private String id;

  @JsonProperty("bank_name")
  @NotBlank(groups = CreateBankAccountValidator.class)
  @Length(max = 50, groups = BankAccountValidator.class)
  private String bankName;

  @JsonProperty("last4")
  @NotBlank(groups = CreateBankAccountValidator.class)
  @Length(max = 4, groups = BankAccountValidator.class)
  private String last4;

}
