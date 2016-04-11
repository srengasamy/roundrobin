package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.User;
import com.roundrobin.domain.UserAction;
import com.roundrobin.domain.UserAction.UserActionType;
import com.roundrobin.repository.ProfileRepository;
import com.roundrobin.repository.UserActionRepository;
import com.roundrobin.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository uRepo;

  @Autowired
  private ProfileRepository pRepo;

  @Autowired
  private UserActionRepository uaRepo;

  @Value("${activation.duration}")
  private int duration;

  @Override
  public User read(String id) {
    Optional<User> user = uRepo.findById(id);
    checkArgument(user.isPresent(), ErrorCodes.INVALID_USER_ID);
    return uRepo.findOne(id);
  }

  @Override
  public User create(User user) {
    user.getProfile().setCreated(LocalDateTime.now());
    pRepo.save(user.getProfile());
    return uRepo.save(user);
  }

  @Override
  public User update(User user) {
    return uRepo.save(user);
  }

  @Override
  public void delete(String id) {
    User user = read(id);
    user.setActive(false);
    uRepo.save(user);
  }

  @Override
  public void sendActivateLink(User user) {
    UserAction action = new UserAction();
    action.setAction(UserActionType.ACTIVATE);
    // action.setExpiry(DateTime.now().plusHours(duration).toDate());
    // action.setCreated(DateTime.now().toDate());
    action.setSecret(UUID.randomUUID().toString());
    uaRepo.save(action);
  }

}
