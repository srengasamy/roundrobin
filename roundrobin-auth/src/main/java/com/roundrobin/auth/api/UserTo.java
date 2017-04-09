package com.roundrobin.auth.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.auth.groups.CreateUserValidator;
import com.roundrobin.auth.groups.UserValidator;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
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

  private List<String> roles = new ArrayList<>();
  private boolean verified;

}
