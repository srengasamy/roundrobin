package com.roundrobin.api;

import org.hibernate.validator.constraints.NotBlank;

import com.roundrobin.groups.UserActionValidator;
import com.roundrobin.groups.UserChangePasswordValidator;

public class UserActionTo {
  @NotBlank(groups = UserActionValidator.class)
  private String id;

  @NotBlank(groups = UserActionValidator.class)
  private String userProfileId;

  @NotBlank(groups = UserActionValidator.class)
  private String secret;

  @NotBlank(groups = UserChangePasswordValidator.class)
  private String password;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
