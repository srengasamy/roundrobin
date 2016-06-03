package com.roundrobin.api;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.roundrobin.groups.RequestUserActionValidator;
import com.roundrobin.groups.ResetPasswordValidator;
import com.roundrobin.groups.UserActionValidator;


public class UserActionTo {

  @NotBlank(groups = UserActionValidator.class)
  private String userId;

  @NotBlank(groups = UserActionValidator.class)
  private String secret;

  @NotBlank(groups = ResetPasswordValidator.class)
  private String password;

  @NotBlank(groups = RequestUserActionValidator.class)
  @Email(groups = RequestUserActionValidator.class)
  private String email;

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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
