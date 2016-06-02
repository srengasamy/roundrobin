package com.roundrobin.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.UserTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.Constants;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.Role;
import com.roundrobin.domain.User;
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
    Assert.isTrue(userAction.isPresent(), ErrorCode.INVALID_USER_ACTION_ID.toString());
    return userAction.get();
  }

  @Override
  public User getByUsername(String username) {
    Optional<User> userAction = Optional.ofNullable(userRepo.findByUsername(username));
    Assert.isTrue(userAction.isPresent(), ErrorCode.INVALID_USERNAME.toString());
    return userAction.get();
  }

  @Override
  public User save(User user) {
    return userRepo.save(user);
  }

  @Override
  public void create(UserTo userTo) {
    User user = new User();
    user.setUsername(userTo.getUsername().get());
    user.setPassword(userTo.getPassword().get());
    user.setVendor(userTo.getVendor().get());
    user.setActive(true);
    user.setVerified(false);
    save(user);
  }

  @Override
  public void update(UserTo userTo) {
    User user = get(userTo.getId());
    Role roleUser = roleService.getByName(Constants.ROLE_USER);
    Role roleVendor = roleService.getByName(Constants.ROLE_VENDOR);
    user.setVendor(userTo.getVendor().orElse(user.getVendor()));
    if (user.getVendor()) {
      if (!user.getRoles().stream().map(c -> c.getId()).collect(Collectors.toList()).contains(roleVendor.getId())) {
        user.getRoles().add(roleVendor);
      }
      if (!user.getRoles().stream().map(c -> c.getId()).collect(Collectors.toList()).contains(roleUser.getId())) {
        user.getRoles().add(roleUser);
      }
    } else if (!user.getVendor()) {
      user.getRoles().removeIf(c -> c.getId().equals(roleVendor.getId()));
      if (!user.getRoles().stream().map(c -> c.getId()).collect(Collectors.toList()).contains(roleUser.getId())) {
        user.getRoles().add(roleUser);
      }
    }
    save(user);
  }

  @Override
  public void delete(String userId) {
    User user = get(userId);
    user.setActive(false);
    save(user);
  }
}
