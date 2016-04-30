package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.UserAction;
import com.roundrobin.domain.UserAction.UserActionType;
import com.roundrobin.domain.UserProfile;
import com.roundrobin.repository.UserActionRepository;

@Service
public class UserActionServiceImpl implements UserActionService {

  @Autowired
  private UserActionRepository userActionRepo;

  @Autowired
  private UserProfileService userProfileService;

  @Override
  public UserAction sendActivationLink() {
    UserAction userAction = new UserAction();
    userAction.setAction(UserActionType.ACTIVATE);
    userAction.setCreated(DateTime.now());
    userAction.setExpiry(DateTime.now().plusHours(48));
    userAction.setSecret("secret");
    return save(userAction);
  }

  @Override
  public void activate(UserActionTo userActionTo) {
    UserAction userAction = get(userActionTo.getId());
    // TODO check secret
    // TODO check expiry time
    // TODO check active
    // TODO change active to false and save
    UserProfile userProfile = userProfileService.get(userActionTo.getUserProfileId());
    userProfile.setActive(true);
    userProfileService.save(userProfile);
  }

  @Override
  public UserAction save(UserAction userAction) {
    return userActionRepo.save(userAction);
  }

  @Override
  public UserAction get(String id) {
    Optional<UserAction> userAction = userActionRepo.findById(id);
    checkArgument(userAction.isPresent(), ErrorCodes.INVALID_USER_ACTION_ID);
    return userAction.get();
  }

}
