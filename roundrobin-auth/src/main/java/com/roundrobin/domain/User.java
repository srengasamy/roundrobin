package com.roundrobin.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mongodb.DBObject;

@Document(collection = "user")
public class User extends Generic implements UserDetails {
  private static final long serialVersionUID = 1L;
  private String username;
  private String password;
  private Boolean vendor;
  private Boolean verified;

  private List<Role> roles = new ArrayList<>();
  private List<UserAction> actions = new ArrayList<>();

  public User() {

  }

  @SuppressWarnings("unchecked")
  public User(DBObject dbObject) {
    this.id = dbObject.get("_id").toString();
    this.username = (String) dbObject.get("username");
    this.password = (String) dbObject.get("password");
    this.vendor = (Boolean) dbObject.get("vendor");
    this.verified = (Boolean) dbObject.get("verified");
    this.roles = (List<Role>) dbObject.get("roles");
  }

  public User(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.active = user.getActive();
    this.verified = user.getVerified();
    this.roles = user.getRoles();
  }

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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(s -> new SimpleGrantedAuthority(s.toString())).collect(Collectors.toList());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }


}
