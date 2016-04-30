package com.roundrobin.services;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.domain.UserAction;

public interface UserActionService {
  public UserAction get(String id);

  public UserAction sendActivationLink();

  public void activate(UserActionTo userActionTo);

  public UserAction save(UserAction userAction);

}
