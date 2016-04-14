package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.User;
import com.roundrobin.domain.UserAction;
import com.roundrobin.domain.UserAction.UserActionType;
import com.roundrobin.repository.UserActionRepository;
import com.roundrobin.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private UserActionRepository userActionRepo;

  @Value("${activation.duration}")
  private int duration;

  @Autowired
  private SkillService skillService;

  @Autowired
  private ProfileService profileService;

  @Override
  public User read(String id) {
    Optional<User> user = userRepo.findById(id);
    checkArgument(user.isPresent(), ErrorCodes.INVALID_USER_ID);
    return user.get();
  }

  @Override
  public User create(User user) {
    user.getProfile().setCreated(DateTime.now());
    profileService.create(user.getProfile());
    user.getSkills().stream().forEach(c -> skillService.create(c));
    return userRepo.save(user);
  }

  @Override
  public User update(User user) {
    Optional<User> existing = userRepo.findById(user.getId());
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_USER_ID);
    return userRepo.save(user);
  }

  @Override
  public void delete(String id) {
    User user = read(id);
    user.setActive(false);
    userRepo.save(user);
  }

  @Override
  public void sendActivateLink(User user) {
    UserAction action = new UserAction();
    action.setAction(UserActionType.ACTIVATE);
    // action.setExpiry(DateTime.now().plusHours(duration).toDate());
    // action.setCreated(DateTime.now().toDate());
    action.setSecret(UUID.randomUUID().toString());
    userActionRepo.save(action);
  }

}
