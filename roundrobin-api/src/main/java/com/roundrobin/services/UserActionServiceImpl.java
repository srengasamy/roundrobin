package com.roundrobin.services;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.UserActionTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.Credential;
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

  @Autowired
  private CredentialService credentialService;

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
    Assert.isTrue(userAction.getSecret().equals(userActionTo.getSecret()), ErrorCode.INVALID_SECRET);
    Assert.isTrue(userAction.getExpiry().isAfterNow(), ErrorCode.ACTION_EXPIRED);
    UserProfile userProfile = userProfileService.get(userActionTo.getUserProfileId());
    Assert.isTrue(userProfile.getActions().stream().map(c -> c.getId()).collect(Collectors.toList()).contains(userActionTo
            .getId()), ErrorCode.INVALID_PROFILE_ID);
    Assert.isTrue(!userProfile.getActive(), ErrorCode.USER_ALREADY_ACTIVE);
    userProfile.setActive(true);
    userProfileService.save(userProfile);
    userAction.setActive(false);
    save(userAction);
  }

  @Override
  public void requestResetPassword(UserActionTo userActionTo) {
    UserProfile userProfile = userProfileService.getByEmail(userActionTo.getEmail());
    userProfile.getActions().add(sendPasswordResetLink());
    userProfileService.save(userProfile);
  }

  @Override
  public void requestActivate(UserActionTo userActionTo) {
    requestActivate(userProfileService.getByEmail(userActionTo.getEmail()));
  }

  @Override
  public void requestActivate(UserProfile userProfile) {
    Assert.isTrue(!userProfile.getActive(), ErrorCode.USER_ALREADY_ACTIVE);
    userProfile.getActions().add(sendActivationLink());
    userProfileService.save(userProfile);
  }

  @Override
  public void resetPassword(UserActionTo userActionTo) {
    UserAction userAction = get(userActionTo.getId());
    Assert.isTrue(userAction.getSecret().equals(userActionTo.getSecret()), ErrorCode.INVALID_SECRET);
    Assert.isTrue(userAction.getExpiry().isAfterNow(), ErrorCode.ACTION_EXPIRED);
    UserProfile userProfile = userProfileService.get(userActionTo.getUserProfileId());
    Assert.isTrue(userProfile.getActions().stream().map(c -> c.getId()).collect(Collectors.toList()).contains(userActionTo
            .getId()), ErrorCode.INVALID_PROFILE_ID);
    Credential credential = userProfile.getCredential();
    credential.setPassword(userActionTo.getPassword());
    credentialService.save(credential);
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
