package com.roundrobin.service;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.api.UserTo;
import com.roundrobin.domain.User;

public interface UserService {
  public User get(String id);

  public User getByUsername(String username);

  public User save(User user);

  public void create(UserTo userTo);

  public void update(UserTo userTo);

  public void delete(String userId);

  public void requestVerify(UserActionTo userActionTo);

  public void requestResetPassword(UserActionTo userActionTo);

  public void verifyUser(UserActionTo userActionTo);

  public void resetPassword(UserActionTo userActionTo);
}
