package com.roundrobin.service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.User;
import com.roundrobin.domain.UserAction;
import com.roundrobin.domain.UserAction.UserActionType;
import com.roundrobin.repository.UserActionRepository;


@Service
public class UserActionServiceImpl implements UserActionService {

  @Autowired
  private UserActionRepository userActionRepo;

  @Autowired
  private UserService userService;

  public UserAction sendActivationLink() {
    UserAction userAction = new UserAction();
    userAction.setAction(UserActionType.ACTIVATE_USER);
    userAction.setCreated(DateTime.now());
    userAction.setExpiry(DateTime.now().plusHours(48));
    userAction.setSecret(UUID.randomUUID().toString());
    return save(userAction);
  }

  public UserAction sendPasswordResetLink() {
    UserAction userAction = new UserAction();
    userAction.setAction(UserActionType.RESET_PASSWORD);
    userAction.setCreated(DateTime.now());
    userAction.setExpiry(DateTime.now().plusHours(48));
    userAction.setSecret(UUID.randomUUID().toString());
    return save(userAction);
  }

  @Override
  public void activateUser(UserActionTo userActionTo) {
    UserAction userAction = get(userActionTo.getId());
    Assert.isTrue(userAction.getSecret().equals(userActionTo.getSecret()), ErrorCode.INVALID_SECRET.toString());
    Assert.isTrue(userAction.getExpiry().isAfterNow(), ErrorCode.ACTION_EXPIRED.toString());
    User user = userService.get(userActionTo.getUserId());
    Assert.isTrue(
        user.getActions().stream().map(c -> c.getId()).collect(Collectors.toList()).contains(userActionTo.getId()),
        ErrorCode.INVALID_PROFILE_ID.toString());
    Assert.isTrue(!user.getActive(), ErrorCode.USER_ALREADY_ACTIVE.toString());
    user.setVerified(true);
    userService.save(user);
    userAction.setActive(false);
    save(userAction);
  }

  @Override
  public void requestResetPassword(UserActionTo userActionTo) {
    User user = userService.getByUsername(userActionTo.getEmail());
    user.getActions().add(sendPasswordResetLink());
    userService.save(user);
  }

  @Override
  public void requestActivate(UserActionTo userActionTo) {
    User user = userService.getByUsername(userActionTo.getEmail());
    Assert.isTrue(!user.getActive(), ErrorCode.USER_ALREADY_ACTIVE.toString());
    user.getActions().add(sendActivationLink());
    userService.save(user);
  }

  @Override
  public void resetPassword(UserActionTo userActionTo) {
    UserAction userAction = get(userActionTo.getId());
    Assert.isTrue(userAction.getSecret().equals(userActionTo.getSecret()), ErrorCode.INVALID_SECRET);
    Assert.isTrue(userAction.getExpiry().isAfterNow(), ErrorCode.ACTION_EXPIRED);
    User user = userService.get(userActionTo.getUserId());
    Assert.isTrue(
        user.getActions().stream().map(c -> c.getId()).collect(Collectors.toList()).contains(userActionTo.getId()),
        ErrorCode.INVALID_PROFILE_ID);
    user.setPassword(userActionTo.getPassword());
    userService.save(user);
    userAction.setActive(false);
    save(userAction);
  }

  @Override
  public UserAction save(UserAction userAction) {
    return userActionRepo.save(userAction);
  }

  @Override
  public UserAction get(String id) {
    Optional<UserAction> userAction = userActionRepo.findById(id);
    Assert.isTrue(userAction.isPresent(), ErrorCode.INVALID_USER_ACTION_ID);
    return userAction.get();
  }

}
