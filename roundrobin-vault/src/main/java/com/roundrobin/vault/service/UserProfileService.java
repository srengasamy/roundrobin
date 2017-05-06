package com.roundrobin.vault.service;

import com.roundrobin.core.api.User;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.UserProfileRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.roundrobin.core.common.ErrorCodes.INVALID_REQUEST;
import static com.roundrobin.core.common.ErrorCodes.INVALID_USER_ID;
import static com.roundrobin.core.common.Preconditions.badRequest;

@Service
public class UserProfileService {
  @Autowired
  private UserProfileRepository profileRepo;

  public UserProfile get(User user) {
    Optional<UserProfile> userProfile = profileRepo.findById(user.getUserId());
    badRequest(userProfile.isPresent(), INVALID_USER_ID);
    return userProfile.get();
  }

  public UserProfile save(UserProfile userProfile) {
    return profileRepo.save(userProfile);
  }

  public UserProfileTo read(User user) {
    return convert(get(user));
  }

  public UserProfileTo create(User user, UserProfileTo userProfileTo) {
    Optional<UserProfile> existing = profileRepo.findById(user.getUserId());
    badRequest(!existing.isPresent(), INVALID_REQUEST);
    UserProfile userProfile = new UserProfile();
    userProfile.setId(user.getUserId());
    userProfile.setFirstName(userProfileTo.getFirstName().get());
    userProfile.setLastName(userProfileTo.getLastName().get());
    userProfile.setDob(userProfileTo.getDob().get());
    userProfile.setMobileNumber(userProfileTo.getMobileNumber().get());
    userProfile.setHomeNumber(userProfileTo.getHomeNumber().orElse(null));
    userProfile.setSex(userProfileTo.getSex().orElse(null));
    userProfile.setVendor(userProfileTo.getVendor().get());
    userProfile.setActive(false);
    userProfile.setCreated(DateTime.now());
    return convert(save(userProfile));
  }

  public UserProfileTo update(User user, UserProfileTo userProfileTo) {
    UserProfile userProfile = get(user);
    userProfile.setFirstName(userProfileTo.getFirstName().orElse(userProfile.getFirstName()));
    userProfile.setLastName(userProfileTo.getLastName().orElse(userProfile.getLastName()));
    userProfile.setDob(userProfileTo.getDob().orElse(userProfile.getDob()));
    userProfile.setMobileNumber(userProfileTo.getMobileNumber().orElse(userProfile.getMobileNumber()));
    userProfile.setHomeNumber(userProfileTo.getHomeNumber().orElse(userProfile.getHomeNumber()));
    userProfile.setSex(userProfileTo.getSex().orElse(userProfile.getSex()));
    userProfile.setVendor(userProfileTo.getVendor().orElse(userProfile.getVendor()));
    return convert(save(userProfile));
  }

  public void delete(User user) {
    UserProfile userProfile = get(user);
    userProfile.setActive(false);
    save(userProfile);
  }

  public UserProfileTo convert(UserProfile userProfile) {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of(userProfile.getFirstName()));
    userProfileTo.setLastName(Optional.of(userProfile.getLastName()));
    userProfileTo.setDob(Optional.of(userProfile.getDob()));
    userProfileTo.setHomeNumber(Optional.ofNullable(userProfile.getHomeNumber()));
    userProfileTo.setMobileNumber(Optional.of(userProfile.getMobileNumber()));
    userProfileTo.setSex(Optional.ofNullable(userProfile.getSex()));
    userProfileTo.setVendor(Optional.of(userProfile.getVendor()));
    return userProfileTo;
  }
}
