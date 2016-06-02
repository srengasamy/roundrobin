package com.roundrobin.api;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.groups.CreateUserValidator;
import com.roundrobin.groups.UpdateUserValidator;
import com.roundrobin.groups.UserValidator;

@JsonInclude(Include.NON_ABSENT)
public class UserTo {
  @NotBlank(groups = UpdateUserValidator.class)
  private String id;

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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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


}
