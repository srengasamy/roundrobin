package com.roundrobin.auth.service;

import com.roundrobin.auth.api.UserActionTo;
import com.roundrobin.auth.api.UserTo;
import com.roundrobin.auth.domain.User;
import com.roundrobin.auth.domain.User.Role;
import com.roundrobin.auth.domain.UserAction;
import com.roundrobin.auth.domain.UserAction.UserActionType;
import com.roundrobin.auth.repository.UserRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.roundrobin.auth.common.ErrorCodes.INVALID_SECRET;
import static com.roundrobin.auth.common.ErrorCodes.INVALID_USERNAME;
import static com.roundrobin.auth.common.ErrorCodes.INVALID_USER_ID;
import static com.roundrobin.auth.common.ErrorCodes.USER_ALREADY_EXIST;
import static com.roundrobin.auth.common.ErrorCodes.USER_ALREADY_VERIFIED;
import static com.roundrobin.core.common.Preconditions.badRequest;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User get(String id) {
    Optional<User> user = userRepo.findById(id);
    badRequest(user.isPresent(), INVALID_USER_ID);
    return user.get();
  }

  public User getByUsername(String username) {
    Optional<User> user = userRepo.findByUsername(username);
    badRequest(user.isPresent(), INVALID_USERNAME);
    return user.get();
  }

  public User save(User user) {
    return userRepo.save(user);
  }

  public UserTo readByUsername(String username) {
    return convert(getByUsername(username));
  }

  public void create(UserTo userTo) {
    Optional<User> existing = userRepo.findByUsername(userTo.getUsername().get());
    badRequest(!existing.isPresent(), USER_ALREADY_EXIST);
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
    User user = getByUsername(userTo.getUsername().get());
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

  public void delete(String username) {
    User user = getByUsername(username);
    user.setActive(false);
    save(user);
  }

  public void requestVerify(UserActionTo userActionTo) {
    User user = getByUsername(userActionTo.getEmail());
    badRequest(!user.getVerified(), USER_ALREADY_VERIFIED);
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
    badRequest(found, INVALID_SECRET);
    badRequest(!user.getVerified(), USER_ALREADY_VERIFIED);
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
    badRequest(found, INVALID_SECRET);
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

  private UserTo convert(User user) {
    UserTo userTo = new UserTo();
    userTo.setUserId(user.getId());
    userTo.setUsername(Optional.of(user.getUsername()));
    userTo.setRoles(user.getRoles().stream().map(r -> r.toString()).collect(Collectors.toList()));
    userTo.setVerified(user.getVerified());
    userTo.setVendor(Optional.of(user.getVendor()));
    return userTo;
  }
}
