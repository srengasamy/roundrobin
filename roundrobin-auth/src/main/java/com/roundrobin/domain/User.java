package com.roundrobin.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User extends Generic {
  private String username;
  private String password;
  private Boolean vendor;
  private Boolean verified;

  private List<Role> roles = new ArrayList<>();
  private List<UserAction> actions = new ArrayList<>();

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

  public static enum Role {
    USER, VENDOR, CLIENT, ADMIN;
  }
}
