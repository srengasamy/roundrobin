package com.roundrobin.api;

import org.hibernate.validator.constraints.NotBlank;

import com.roundrobin.groups.UserActivationValidator;

public class UserActionTo {
  @NotBlank(groups = UserActivationValidator.class)
  private String id;

  @NotBlank(groups = UserActivationValidator.class)
  private String userProfileId;

  @NotBlank(groups = UserActivationValidator.class)
  private String secret;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getUserProfileId() {
    return userProfileId;
  }

  public void setUserProfileId(String userProfileId) {
    this.userProfileId = userProfileId;
  }

}
