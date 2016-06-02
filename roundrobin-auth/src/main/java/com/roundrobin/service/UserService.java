package com.roundrobin.service;

import com.roundrobin.api.UserTo;
import com.roundrobin.domain.User;

public interface UserService {
  public User get(String id);

  public User getByUsername(String username);

  public User save(User user);

  public void create(UserTo userTo);

  public void update(UserTo userTo);

  public void delete(String userId);
}
