package com.roundrobin.auth.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {
  @Id
  @Indexed
  private String id;
  @Indexed
  private String username;
  private String password;
  private Boolean vendor;
  private Boolean verified;

  private List<Role> roles = new ArrayList<>();
  private List<UserAction> actions = new ArrayList<>();
  @CreatedDate
  private Date created;

  private Boolean active;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getVendor() {
    return vendor;
  }

  public void setVendor(Boolean vendor) {
    this.vendor = vendor;
  }

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public List<UserAction> getActions() {
    return actions;
  }

  public void setActions(List<UserAction> actions) {
    this.actions = actions;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public static enum Role {
    USER, VENDOR, CLIENT, ADMIN;
  }
}
