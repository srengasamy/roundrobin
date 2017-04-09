package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.vault.groups.UpdateCredentialValidator;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
@JsonInclude(Include.NON_ABSENT)
public class CredentialTo {
  @NotBlank(groups = UpdateCredentialValidator.class)
  private String id;

  @JsonProperty("user_profile_id")
  @NotBlank(groups = UpdateCredentialValidator.class)
  private String userProfileId;

  @NotBlank(groups = UpdateCredentialValidator.class)
  private String username;

  @NotBlank(groups = UpdateCredentialValidator.class)
  private String password;

  @NotBlank(groups = UpdateCredentialValidator.class)
  private String token;

  public CredentialTo() {
  }

  public CredentialTo(String username, String password) {
    this.username = username;
    this.password = password;
  }

}
