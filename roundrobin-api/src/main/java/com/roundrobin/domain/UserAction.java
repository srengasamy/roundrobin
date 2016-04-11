package com.roundrobin.domain;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@Document(collection = "user_action")
public class UserAction {
  @Id
  private String id;
  private UserActionType action;
  private String secret;
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private Date expiry;
  @CreatedDate
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private Date created;

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

  public Date getExpiry() {
    return expiry;
  }

  public void setExpiry(Date expiry) {
    this.expiry = expiry;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }
  public static enum UserActionType {
    ACTIVATE, PASSWORD
  }

}
