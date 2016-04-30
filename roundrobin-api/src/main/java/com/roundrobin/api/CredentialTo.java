package com.roundrobin.api;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.groups.UpdateCredentialValidator;

@JsonInclude(Include.NON_ABSENT)
public class CredentialTo {
  @NotBlank(groups = UpdateCredentialValidator.class)
  private String id;
  @NotBlank(groups = UpdateCredentialValidator.class)
  private String userProfileId;
  @NotBlank(groups = UpdateCredentialValidator.class)
  private String username;
  @NotBlank(groups = UpdateCredentialValidator.class)
  private String password;
  @NotBlank(groups = UpdateCredentialValidator.class)
  private String token;

  public CredentialTo() {}

  public CredentialTo(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
