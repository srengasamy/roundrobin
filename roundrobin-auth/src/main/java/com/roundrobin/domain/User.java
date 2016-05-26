package com.roundrobin.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by rengasu on 5/25/16.
 */
@Document(collection = "user")
public class User {
  private String id;
  private String username;
  private String password;
  private List<Role> roles;

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
}
