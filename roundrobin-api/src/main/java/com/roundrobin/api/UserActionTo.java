package com.roundrobin.api;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.roundrobin.groups.UserActionValidator;
import com.roundrobin.groups.UserResetPasswordValidator;
import com.roundrobin.groups.RequestResetPasswordValidator;


public class UserActionTo {
  @NotBlank(groups = UserActionValidator.class)
  private String id;

  @NotBlank(groups = UserActionValidator.class)
  private String userProfileId;

  @NotBlank(groups = UserActionValidator.class)
  private String secret;

  @NotBlank(groups = UserResetPasswordValidator.class)
  private String password;

  @NotBlank(groups = RequestResetPasswordValidator.class)
  @Email(groups = RequestResetPasswordValidator.class)
  private String email;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
