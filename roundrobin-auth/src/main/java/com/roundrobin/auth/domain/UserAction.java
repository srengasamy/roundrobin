package com.roundrobin.auth.domain;

import org.joda.time.DateTime;


public class UserAction {
  private UserActionType action;
  private String secret;
  private DateTime expiry;
  private Boolean active;
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

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public static enum UserActionType {
    VERIFY_USER, RESET_PASSWORD
  }

}
