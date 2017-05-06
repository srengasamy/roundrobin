package com.roundrobin.gate.service;

import com.roundrobin.core.api.User;
import com.roundrobin.gate.api.UserProfileTo;
import com.roundrobin.gate.domain.UserProfile;
import com.roundrobin.gate.repository.UserProfileRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.roundrobin.core.common.ErrorCodes.INVALID_USER_ID;
import static com.roundrobin.core.common.Preconditions.badRequest;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_CLOCK_TIME;

@Service
public class UserProfileService {
  @Autowired
  private UserProfileRepository profileRepo;

  public UserProfile get(User user) {
    Optional<UserProfile> userProfile = profileRepo.findById(user.getUserId());
    return userProfile.orElse(create(user));
  }

  public UserProfile save(UserProfile userProfile) {
    return profileRepo.save(userProfile);
  }

  public UserProfileTo read(User user) {
    return convert(get(user));
  }

  public UserProfile create(User user) {
    UserProfile userProfile = new UserProfile();
    userProfile.setId(user.getUserId());
    userProfile.setFlags(0);
    userProfile.setRadius((short) 5);
    userProfile.setClockIn((short) 540);
    userProfile.setClockOut((short) 1020);
    userProfile.setOvertime((short) 15);
    userProfile.setCreated(DateTime.now());
    return save(userProfile);
  }

  public UserProfileTo create(User user, UserProfileTo userProfileTo) {
    badRequest(userProfileTo.getClockIn() < userProfileTo.getClockOut(), INVALID_CLOCK_TIME);
    UserProfile userProfile = get(user);
    userProfile.setClockIn(userProfileTo.getClockIn());
    userProfile.setClockOut(userProfileTo.getClockOut());
    userProfile.setLocation(userProfileTo.getLocation());
    userProfile.setOvertime(userProfileTo.getOvertime());
    userProfile.setRadius(userProfileTo.getRadius());
    return convert(save(userProfile));
  }

  public UserProfileTo update(User user, UserProfileTo userProfileTo) {
    badRequest(userProfileTo.getClockIn() < userProfileTo.getClockOut(), INVALID_CLOCK_TIME);
    UserProfile userProfile = get(user);
    if (userProfileTo.getRadius() != null) {
      userProfile.setRadius(userProfileTo.getRadius());
    }
    if (userProfileTo.getOvertime() != null) {
      userProfile.setOvertime(userProfileTo.getOvertime());
    }
    if (userProfileTo.getClockIn() != null) {
      userProfile.setClockIn(userProfileTo.getClockIn());
    }
    if (userProfileTo.getClockOut() != null) {
      userProfile.setClockOut(userProfileTo.getClockOut());
    }
    if (userProfileTo.getLocation() != null) {
      userProfile.setLocation(userProfileTo.getLocation());
    }
    return convert(save(userProfile));
  }

  public UserProfileTo convert(UserProfile userProfile) {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setClockIn(userProfile.getClockIn());
    userProfileTo.setClockOut(userProfile.getClockOut());
    userProfileTo.setLocation(userProfile.getLocation());
    userProfileTo.setOvertime(userProfile.getOvertime());
    userProfileTo.setRadius(userProfile.getRadius());
    return userProfileTo;
  }
}
