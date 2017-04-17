package com.roundrobin.auth.service;

import com.roundrobin.auth.api.UserActionTo;
import com.roundrobin.auth.domain.User;
import com.roundrobin.auth.domain.UserAction;
import com.roundrobin.auth.enums.UserActionType;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.roundrobin.auth.common.ErrorCodes.INVALID_SECRET;
import static com.roundrobin.auth.common.ErrorCodes.USER_ALREADY_VERIFIED;
import static com.roundrobin.auth.enums.UserActionType.RESET_PASSWORD;
import static com.roundrobin.auth.enums.UserActionType.VERIFY_USER;
import static com.roundrobin.core.common.Preconditions.badRequest;

/**
 * Created by rengasu on 2/27/17.
 */
@Service
public class UserActionService {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public void create(User user, UserActionType actionType) {
    UserAction userAction = new UserAction();
    userAction.setAction(actionType);
    userAction.setCreated(DateTime.now());
    userAction.setExpiry(DateTime.now().plusHours(48));
    userAction.setSecret(UUID.randomUUID().toString());
    userAction.setActive(true);
    user.getActions().add(userAction);
    userService.save(user);
  }

  public void requestVerify(UserActionTo userActionTo) {
    User user = userService.getByUsername(userActionTo.getEmail());
    badRequest(!user.getVerified(), USER_ALREADY_VERIFIED);
    create(user, VERIFY_USER);
  }

  public void requestResetPassword(UserActionTo userActionTo) {
    User user = userService.getByUsername(userActionTo.getEmail());
    create(user, RESET_PASSWORD);
  }

  public void verifyUser(UserActionTo userActionTo) {
    User user = userService.get(userActionTo.getUserId());
    boolean found = user.getActions().stream().anyMatch(a -> (a.getActive()
            && a.getAction() == UserActionType.VERIFY_USER
            && a.getSecret().equals(userActionTo.getSecret())
            && a.getExpiry().isAfterNow()));
    badRequest(found, INVALID_SECRET);
    badRequest(!user.getVerified(), USER_ALREADY_VERIFIED);
    user.setVerified(true);
    user.getActions().forEach(a -> {
      if (a.getAction() == UserActionType.VERIFY_USER && a.getSecret().equals(userActionTo.getSecret())) {
        a.setActive(false);
      }
    });
    userService.save(user);
  }

  public void resetPassword(UserActionTo userActionTo) {
    User user = userService.get(userActionTo.getUserId());
    boolean found = user.getActions().stream().anyMatch(a -> (a.getActive()
            && a.getAction() == UserActionType.RESET_PASSWORD
            && a.getSecret().equals(userActionTo.getSecret())
            && a.getExpiry().isAfterNow()));
    badRequest(found, INVALID_SECRET);
    user.setPassword(passwordEncoder.encode(userActionTo.getPassword()));
    user.setVerified(true);
    user.getActions().forEach(a -> {
      if (a.getAction() == UserActionType.RESET_PASSWORD && a.getSecret().equals(userActionTo.getSecret())) {
        a.setActive(false);
      }
    });
    userService.save(user);
  }
}
