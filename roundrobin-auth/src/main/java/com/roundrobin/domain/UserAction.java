package com.roundrobin.domain;

import org.joda.time.DateTime;


public class UserAction {
  private UserActionType action;
  private String secret;
  private DateTime expiry;
  private DateTime created;

  public UserActionType getAction() {
    return action;
  }

  public void setAction(UserActionType action) {
    this.action = action;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public DateTime getExpiry() {
    return expiry;
  }

  public void setExpiry(DateTime expiry) {
    this.expiry = expiry;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public static enum UserActionType {
    VERIFY_USER, RESET_PASSWORD
  }

}
