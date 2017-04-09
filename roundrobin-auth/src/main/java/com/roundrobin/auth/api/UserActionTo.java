package com.roundrobin.auth.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.auth.groups.RequestUserActionValidator;
import com.roundrobin.auth.groups.ResetPasswordValidator;
import com.roundrobin.auth.groups.UserActionValidator;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class UserActionTo {

  @JsonProperty("user_id")
  @NotBlank(groups = UserActionValidator.class)
  private String userId;

  @NotBlank(groups = UserActionValidator.class)
  private String secret;

  @NotBlank(groups = ResetPasswordValidator.class)
  private String password;

  @NotBlank(groups = RequestUserActionValidator.class)
  @Email(groups = RequestUserActionValidator.class)
  private String email;

}
