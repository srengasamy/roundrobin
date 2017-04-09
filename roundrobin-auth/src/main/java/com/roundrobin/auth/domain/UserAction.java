package com.roundrobin.auth.domain;

import com.roundrobin.auth.enums.UserActionType;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class UserAction {
  private UserActionType action;
  private String secret;
  private DateTime expiry;
  private Boolean active;
  private DateTime created;

}
