package com.roundrobin.auth.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.auth.groups.CreateUserValidator;
import com.roundrobin.auth.groups.UserValidator;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class UserTo {
  @JsonProperty("user_id")
  private String userId;

  @NotBlank(groups = CreateUserValidator.class)
  @Email(groups = UserValidator.class)
  private String username;

  @NotBlank(groups = CreateUserValidator.class)
  @Length(max = 35, groups = UserValidator.class)
  private String password;

  private List<String> roles = new ArrayList<>();
  private boolean verified;

}
