package com.roundrobin.service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.api.UserTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.Constants;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.Role;
import com.roundrobin.domain.User;
import com.roundrobin.domain.UserAction;
import com.roundrobin.domain.UserAction.UserActionType;
import com.roundrobin.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepo;

  @Autowired
  private RoleService roleService;

  @Override
  public User get(String id) {
    Optional<User> userAction = userRepo.findById(id);
    Assert.isTrue(userAction.isPresent(), ErrorCode.INVALID_USER_ID);
    return userAction.get();
  }

  @Override
  public User getByUsername(String username) {
    Optional<User> userAction = Optional.ofNullable(userRepo.findByUsername(username));
    Assert.isTrue(userAction.isPresent(), ErrorCode.INVALID_USERNAME);
    return userAction.get();
  }

  @Override
  public User save(User user) {
    return userRepo.save(user);
  }

  @Override
  public void create(UserTo userTo) {
    Optional<User> existing = Optional.ofNullable(userRepo.findByUsername(userTo.getUsername().get()));
    Assert.isTrue(!existing.isPresent(), ErrorCode.USER_ALREADY_EXIST);
    Role roleUser = roleService.getByName(Constants.ROLE_USER);
    Role roleVendor = roleService.getByName(Constants.ROLE_VENDOR);
    User user = new User();
    user.setUsername(userTo.getUsername().get());
    user.setPassword(userTo.getPassword().get());
    user.setVendor(userTo.getVendor().get());
    user.setActive(true);
    user.setVerified(false);
    user.getRoles().add(roleUser);
    if (user.getVendor()) {
      user.getRoles().add(roleVendor);
    }
    user.getActions().add(createVerifyLink());
    save(user);
  }

  @Override
  public void update(UserTo userTo) {
    User user = get(userTo.getUserId());
    Role roleVendor = roleService.getByName(Constants.ROLE_VENDOR);
    user.setVendor(userTo.getVendor().orElse(user.getVendor()));
    if (user.getVendor()) {
      if (!user.getRoles().stream().map(c -> c.getId()).collect(Collectors.toList()).contains(roleVendor.getId())) {
        user.getRoles().add(roleVendor);
      }
    } else if (!user.getVendor()) {
      user.getRoles().removeIf(c -> c.getId().equals(roleVendor.getId()));
    }
    save(user);
  }

  @Override
  public void delete(String userId) {
    User user = get(userId);
    user.setActive(false);
    save(user);
  }

  @Override
  public void requestVerify(UserActionTo userActionTo) {
    User user = getByUsername(userActionTo.getEmail());
    Assert.isTrue(!user.getVerified(), ErrorCode.USER_ALREADY_VERIFIED);
    user.getActions().add(createVerifyLink());
    save(user);
  }

  @Override
  public void requestResetPassword(UserActionTo userActionTo) {
    User user = getByUsername(userActionTo.getEmail());
    user.getActions().add(createPasswordResetLink());
    save(user);
  }

  @Override
  public void verifyUser(UserActionTo userActionTo) {
    User user = get(userActionTo.getUserId());
    boolean found = user.getActions().stream().anyMatch(a -> (a.getAction() == UserActionType.VERIFY_USER
        && a.getSecret().equals(userActionTo.getSecret()) && a.getExpiry().isAfterNow()));
    Assert.isTrue(found, ErrorCode.INVALID_SECRET);
    Assert.isTrue(!user.getVerified(), ErrorCode.USER_ALREADY_VERIFIED);
    user.setVerified(true);
    user.getActions().removeIf(a -> (a.getAction() == UserActionType.VERIFY_USER
        && a.getSecret().equals(userActionTo.getSecret()) && a.getExpiry().isAfterNow()));
    save(user);
  }

  @Override
  public void resetPassword(UserActionTo userActionTo) {
    User user = get(userActionTo.getUserId());
    boolean found = user.getActions().stream().anyMatch(a -> (a.getAction() == UserActionType.RESET_PASSWORD
        && a.getSecret().equals(userActionTo.getSecret()) && a.getExpiry().isAfterNow()));
    Assert.isTrue(found, ErrorCode.INVALID_SECRET);
    user.setPassword(userActionTo.getPassword());
    save(user);
  }

  public UserAction createVerifyLink() {
    UserAction userAction = new UserAction();
    userAction.setAction(UserActionType.VERIFY_USER);
    userAction.setCreated(DateTime.now());
    userAction.setExpiry(DateTime.now().plusHours(48));
    userAction.setSecret(UUID.randomUUID().toString());
    return userAction;
  }

  public UserAction createPasswordResetLink() {
    UserAction userAction = new UserAction();
    userAction.setAction(UserActionType.RESET_PASSWORD);
    userAction.setCreated(DateTime.now());
    userAction.setExpiry(DateTime.now().plusHours(48));
    userAction.setSecret(UUID.randomUUID().toString());
    return userAction;
  }
}
