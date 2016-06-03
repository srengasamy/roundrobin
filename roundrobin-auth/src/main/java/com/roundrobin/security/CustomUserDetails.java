package com.roundrobin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roundrobin.domain.Role;
import com.roundrobin.domain.User;


public class CustomUserDetails implements UserDetails {
  private static final long serialVersionUID = 1L;
  private Collection<? extends GrantedAuthority> authorities;
  private String password;
  private String username;
  private String userId;
  private Boolean enabled;
  private Boolean verified;

  public CustomUserDetails() {

  }

  public CustomUserDetails(User user) {
    this.userId = user.getId();
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.authorities = translate(user.getRoles());
    this.enabled = user.getActive();
    this.verified = user.getVerified();
  }

  private Collection<? extends GrantedAuthority> translate(List<Role> roles) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role : roles) {
      String name = role.getName();
      authorities.add(new SimpleGrantedAuthority(name));
    }
    return authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @Override
  @JsonIgnore
  public String getUsername() {
    return username;
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
    return enabled;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

}
