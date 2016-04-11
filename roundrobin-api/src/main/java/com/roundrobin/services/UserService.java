package com.roundrobin.services;

import com.roundrobin.domain.User;

public interface UserService {
  public User read(String id);

  public User create(User user);

  public User update(User user);

  public void delete(String id);

  public void sendActivateLink(User user);
}
