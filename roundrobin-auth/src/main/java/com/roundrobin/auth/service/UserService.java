package com.roundrobin.auth.service;

import com.roundrobin.auth.api.UserTo;
import com.roundrobin.auth.domain.User;
import com.roundrobin.auth.enums.RoleType;
import com.roundrobin.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.roundrobin.auth.common.ErrorCodes.INVALID_USERNAME;
import static com.roundrobin.auth.common.ErrorCodes.INVALID_USER_ID;
import static com.roundrobin.auth.common.ErrorCodes.USER_ALREADY_EXIST;
import static com.roundrobin.auth.enums.UserActionType.VERIFY_USER;
import static com.roundrobin.core.common.Preconditions.badRequest;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepo;

  @Autowired
  private UserActionService userActionService;

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

  public UserTo read(String userId) {
    return convert(get(userId));
  }

  public void create(UserTo userTo) {
    Optional<User> existing = userRepo.findByUsername(userTo.getUsername().get());
    badRequest(!existing.isPresent(), USER_ALREADY_EXIST);
    User user = new User();
    user.setUsername(userTo.getUsername().get());
    user.setPassword(passwordEncoder.encode(userTo.getPassword().get()));
    user.setActive(true);
    user.setVerified(false);
    user.getRoles().add(RoleType.USER);
    save(user);
    userActionService.create(user, VERIFY_USER);
  }

  public void delete(String userId) {
    User user = get(userId);
    user.setActive(false);
    save(user);
  }

  private UserTo convert(User user) {
    UserTo userTo = new UserTo();
    userTo.setUserId(user.getId());
    userTo.setUsername(Optional.of(user.getUsername()));
    userTo.setRoles(user.getRoles().stream().map(r -> r.toString()).collect(Collectors.toList()));
    userTo.setVerified(user.getVerified());
    return userTo;
  }
}
