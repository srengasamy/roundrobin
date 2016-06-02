package com.roundrobin.service;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.domain.UserAction;

public interface UserActionService {
  public UserAction get(String id);

  public void activateUser(UserActionTo userActionTo);

  public void resetPassword(UserActionTo userActionTo);

  public void requestResetPassword(UserActionTo userActionTo);

  public void requestActivate(UserActionTo userActionTo);

  public UserAction save(UserAction userAction);

}
