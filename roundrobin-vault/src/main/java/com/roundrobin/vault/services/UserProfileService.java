package com.roundrobin.vault.services;

import static com.roundrobin.conditions.Preconditions.checkArgument;
import static com.roundrobin.vault.error.VaultErrorCode.INVALID_USER_ID;

import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.exception.BadRequestException;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.repository.UserProfileRepository;

@Service
public class UserProfileService {
  @Autowired
  private UserProfileRepository profileRepo;

  public UserProfile getByUserId(String userId) {
    Optional<UserProfile> userProfile = profileRepo.findByUserId(userId);
    checkArgument(userProfile.isPresent(), new BadRequestException(INVALID_USER_ID));
    return userProfile.get();
  }

  public UserProfile save(UserProfile userProfile) {
    return profileRepo.save(userProfile);
  }

  public UserProfileTo read(String userId) {
    return convert(getByUserId(userId));
  }

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
    userProfile.setUserId(userProfileTo.getUserId());
    userProfile.setFlags(0);
    userProfile.setActive(false);
    userProfile.setCreated(DateTime.now());
    save(userProfile);
    return convert(userProfile);
  }

  public UserProfileTo update(UserProfileTo userProfileTo) {
    UserProfile userProfile = getByUserId(userProfileTo.getUserId());
    userProfile.setFirstName(userProfileTo.getFirstName().orElse(userProfile.getFirstName()));
    userProfile.setLastName(userProfileTo.getLastName().orElse(userProfile.getLastName()));
    userProfile.setDob(userProfileTo.getDob().orElse(userProfile.getDob()));
    userProfile.setMobileNumber(userProfileTo.getMobileNumber().orElse(userProfile.getMobileNumber()));
    userProfile.setHomeNumber(userProfileTo.getHomeNumber().orElse(userProfile.getHomeNumber()));
    userProfile.setSex(userProfileTo.getSex().orElse(userProfile.getSex()));
    userProfile.setVendor(userProfileTo.getVendor().orElse(userProfile.getVendor()));
    userProfile.setLocation(userProfileTo.getLocation().orElse(userProfile.getLocation()));
    save(userProfile);
    return convert(userProfile);
  }

  public void delete(String userId) {
    UserProfile userProfile = getByUserId(userId);
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
    userProfileTo.setLocation(Optional.ofNullable(userProfile.getLocation()));
    return userProfileTo;
  }
}
