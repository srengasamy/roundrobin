package com.roundrobin.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.groups.CreateUserValidator;
import com.roundrobin.groups.UserValidator;

@JsonInclude(Include.NON_ABSENT)
public class UserTo {
  private String userId;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateUserValidator.class)
  @Email(groups = UserValidator.class)
  private Optional<String> username = Optional.empty();

  @UnwrapValidatedValue
  @NotBlank(groups = CreateUserValidator.class)
  @Length(max = 35, groups = UserValidator.class)
  private Optional<String> password = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateUserValidator.class)
  private Optional<Boolean> vendor = Optional.empty();

  private List<String> roles = new ArrayList<>();
  private boolean verified;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Optional<String> getUsername() {
    return username;
  }

  public void setUsername(Optional<String> username) {
    this.username = username;
  }

  public Optional<String> getPassword() {
    return password;
  }

  public void setPassword(Optional<String> password) {
    this.password = password;
  }

  public Optional<Boolean> getVendor() {
    return vendor;
  }

  public void setVendor(Optional<Boolean> vendor) {
    this.vendor = vendor;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }


}
