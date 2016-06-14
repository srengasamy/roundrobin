package com.roundrobin.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.api.UserTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.User;
import com.roundrobin.domain.User.Role;
import com.roundrobin.domain.UserAction;
import com.roundrobin.domain.UserAction.UserActionType;
import com.roundrobin.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User get(String id) {
    Optional<User> userAction = userRepo.findById(id);
    Assert.isTrue(userAction.isPresent(), ErrorCode.INVALID_USER_ID);
    return userAction.get();
  }

  public User getByUsername(String username) {
    Optional<User> userAction = userRepo.findByUsername(username);
    Assert.isTrue(userAction.isPresent(), ErrorCode.INVALID_USERNAME);
    return userAction.get();
  }

  public User save(User user) {
    return userRepo.save(user);
  }

  public void create(UserTo userTo) {
    Optional<User> existing = userRepo.findByUsername(userTo.getUsername().get());
    Assert.isTrue(!existing.isPresent(), ErrorCode.USER_ALREADY_EXIST);
    User user = new User();
    user.setUsername(userTo.getUsername().get());
    user.setPassword(passwordEncoder.encode(userTo.getPassword().get()));
    user.setVendor(userTo.getVendor().get());
    user.setActive(true);
    user.setVerified(false);
    user.getRoles().add(Role.USER);
    if (user.getVendor()) {
      user.getRoles().add(Role.VENDOR);
    }
    user.getActions().add(createVerifyLink());
    save(user);
  }

  public void update(UserTo userTo) {
    User user = get(userTo.getUserId());
    user.setVendor(userTo.getVendor().orElse(user.getVendor()));
    if (user.getVendor()) {
      if (!user.getRoles().stream().collect(Collectors.toList()).contains(Role.VENDOR)) {
        user.getRoles().add(Role.VENDOR);
      }
    } else if (!user.getVendor()) {
      user.getRoles().removeIf(c -> c.equals(Role.VENDOR));
    }
    save(user);
  }

  public void delete(String userId) {
    User user = get(userId);
    user.setActive(false);
    save(user);
  }

  public void requestVerify(UserActionTo userActionTo) {
    User user = getByUsername(userActionTo.getEmail());
    Assert.isTrue(!user.getVerified(), ErrorCode.USER_ALREADY_VERIFIED);
    user.getActions().add(createVerifyLink());
    save(user);
  }

  public void requestResetPassword(UserActionTo userActionTo) {
    User user = getByUsername(userActionTo.getEmail());
    user.getActions().add(createPasswordResetLink());
    save(user);
  }

  public void verifyUser(UserActionTo userActionTo) {
    User user = get(userActionTo.getUserId());
    boolean found =
        user.getActions().stream().anyMatch(a -> (a.getActive() && a.getAction() == UserActionType.VERIFY_USER
            && a.getSecret().equals(userActionTo.getSecret()) && a.getExpiry().isAfterNow()));
    Assert.isTrue(found, ErrorCode.INVALID_SECRET);
    Assert.isTrue(!user.getVerified(), ErrorCode.USER_ALREADY_VERIFIED);
    user.setVerified(true);
    user.getActions().forEach(a -> {
      if (a.getAction() == UserActionType.VERIFY_USER && a.getSecret().equals(userActionTo.getSecret())) {
        a.setActive(false);
      }
    });
    save(user);
  }

  public void resetPassword(UserActionTo userActionTo) {
    User user = get(userActionTo.getUserId());
    boolean found =
        user.getActions().stream().anyMatch(a -> (a.getActive() && a.getAction() == UserActionType.RESET_PASSWORD
            && a.getSecret().equals(userActionTo.getSecret()) && a.getExpiry().isAfterNow()));
    Assert.isTrue(found, ErrorCode.INVALID_SECRET);
    user.setPassword(passwordEncoder.encode(userActionTo.getPassword()));
    user.setVerified(true);
    user.getActions().forEach(a -> {
      if (a.getAction() == UserActionType.RESET_PASSWORD && a.getSecret().equals(userActionTo.getSecret())) {
        a.setActive(false);
      }
    });
    save(user);
  }

  public UserAction createVerifyLink() {
    UserAction userAction = new UserAction();
    userAction.setAction(UserActionType.VERIFY_USER);
    userAction.setCreated(DateTime.now());
    userAction.setExpiry(DateTime.now().plusHours(48));
    userAction.setSecret(UUID.randomUUID().toString());
    userAction.setActive(true);
    return userAction;
  }

  public UserAction createPasswordResetLink() {
    UserAction userAction = new UserAction();
    userAction.setAction(UserActionType.RESET_PASSWORD);
    userAction.setCreated(DateTime.now());
    userAction.setExpiry(DateTime.now().plusHours(48));
    userAction.setSecret(UUID.randomUUID().toString());
    userAction.setActive(true);
    return userAction;
  }

  public List<GrantedAuthority> getRights(User user) {
    return user.getRoles().stream().map(s -> new SimpleGrantedAuthority(s.toString())).collect(Collectors.toList());
  }
}
