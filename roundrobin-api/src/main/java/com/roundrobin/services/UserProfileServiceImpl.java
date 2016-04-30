package com.roundrobin.services;

import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.api.CredentialTo;
import com.roundrobin.api.UserProfileTo;
import com.roundrobin.common.Assert;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.Credential;
import com.roundrobin.domain.UserProfile;
import com.roundrobin.repository.UserProfileRepository;

@Service
public class UserProfileServiceImpl implements UserProfileService {
  @Autowired
  private UserProfileRepository profileRepo;

  @Autowired
  private CredentialService credentialService;

  @Autowired
  private UserActionService userActionService;

  @Override
  public UserProfile get(String id) {
    Optional<UserProfile> userProfile = profileRepo.findById(id);
    Assert.isTrue(userProfile.isPresent(), ErrorCode.INVALID_PROFILE_ID);
    return userProfile.get();
  }

  @Override
  public UserProfile save(UserProfile userProfile) {
    return profileRepo.save(userProfile);
  }

  @Override
  public UserProfileTo read(String id) {
    UserProfile userProfile = get(id);
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setId(userProfile.getId());
    userProfileTo.setFirstName(Optional.of(userProfile.getFirstName()));
    userProfileTo.setLastName(Optional.of(userProfile.getLastName()));
    userProfileTo.setDob(Optional.of(userProfile.getDob()));
    userProfileTo.setHomeNumber(Optional.ofNullable(userProfile.getHomeNumber()));
    userProfileTo.setMobileNumber(Optional.of(userProfile.getMobileNumber()));
    userProfileTo.setSex(Optional.ofNullable(userProfile.getSex()));
    userProfileTo.setVendor(Optional.of(userProfile.getVendor()));
    userProfileTo.setLocation(Optional.ofNullable(userProfile.getLocation()));
    return userProfileTo;
  }

  @Override
  public UserProfileTo create(UserProfileTo userProfileTo) {
    UserProfile userProfile = new UserProfile();
    userProfile.setFirstName(userProfileTo.getFirstName().get());
    userProfile.setLastName(userProfileTo.getLastName().get());
    userProfile.setDob(userProfileTo.getDob().get());
    userProfile.setEmail(userProfileTo.getEmail().get());
    userProfile.setMobileNumber(userProfileTo.getMobileNumber().get());
    userProfile.setHomeNumber(userProfileTo.getHomeNumber().orElse(null));
    userProfile.setSex(userProfileTo.getSex().orElse(null));
    userProfile.setLocation(userProfileTo.getLocation().orElse(null));
    userProfile.setVendor(userProfileTo.getVendor().get());
    userProfile.setFlags(0);
    userProfile.setActive(false);
    userProfile.setCreated(DateTime.now());
    Credential credential =
        credentialService.create(new CredentialTo(userProfileTo.getEmail().get(), userProfileTo.getPassword().get()));
    userProfile.getActions().add(userActionService.sendActivationLink());
    userProfile.setCredential(credential);
    return read(save(userProfile).getId());
  }

  @Override
  public UserProfileTo update(UserProfileTo userProfileTo) {
    UserProfile userProfile = get(userProfileTo.getId());
    userProfile.setFirstName(userProfileTo.getFirstName().orElse(userProfile.getFirstName()));
    userProfile.setLastName(userProfileTo.getLastName().orElse(userProfile.getLastName()));
    userProfile.setDob(userProfileTo.getDob().orElse(userProfile.getDob()));
    userProfile.setMobileNumber(userProfileTo.getMobileNumber().orElse(userProfile.getMobileNumber()));
    userProfile.setHomeNumber(userProfileTo.getHomeNumber().orElse(userProfile.getHomeNumber()));
    userProfile.setSex(userProfileTo.getSex().orElse(userProfile.getSex()));
    userProfile.setVendor(userProfileTo.getVendor().orElse(userProfile.getVendor()));
    userProfile.setLocation(userProfileTo.getLocation().orElse(userProfile.getLocation()));
    return read(save(userProfile).getId());
  }

  @Override
  public void delete(String id) {
    UserProfile userProfile = get(id);
    userProfile.setActive(false);
    save(userProfile);
  }

}
