package com.roundrobin.services;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.domain.UserAction;
import com.roundrobin.domain.UserProfile;

public interface UserActionService {
  public UserAction get(String id);

  public void activateUser(UserActionTo userActionTo);

  public void requestResetPassword(UserActionTo userActionTo);

  public void requestActivate(UserActionTo userActionTo);

  public void requestActivate(UserProfile userProfile);

  public void resetPassword(UserActionTo userActionTo);

  public UserAction save(UserAction userAction);

}
