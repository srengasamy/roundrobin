package com.roundrobin.vault.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "user_action")
public class UserAction {
  @Id
  private String id;
  private UserActionType action;
  private String secret;
  private DateTime expiry;
  private DateTime created;
  private Boolean active;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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
    ACTIVATE_USER, RESET_PASSWORD
  }

}
