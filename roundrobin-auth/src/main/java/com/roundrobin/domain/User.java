package com.roundrobin.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {
  @Id
  private String id;
  private String username;
  private String password;
  private Boolean vendor;
  private Boolean verified;
  private Boolean active;

  @DBRef
  private List<Role> roles = new ArrayList<>();

  private List<UserAction> actions = new ArrayList<>();

  public User() {

  }

  public User(String username, String password, List<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public List<UserAction> getActions() {
    return actions;
  }

  public void setActions(List<UserAction> actions) {
    this.actions = actions;
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


}
