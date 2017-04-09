package com.roundrobin.auth.domain;

import com.roundrobin.auth.enums.RoleType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class UserDetail implements UserDetails {

  private static final long serialVersionUID = 1L;
  private String userId;
  private String username;
  private String password;
  private boolean active;
  private boolean verified;
  private List<RoleType> roles;

  public UserDetail(User user) {
    username = user.getId();//user.getUsername();
    password = user.getPassword();
    active = user.getActive();
    roles = user.getRoles();
    userId = user.getId();
    verified = user.getVerified();
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
