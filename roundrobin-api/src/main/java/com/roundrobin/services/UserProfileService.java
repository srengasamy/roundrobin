package com.roundrobin.services;

import com.roundrobin.api.UserProfileTo;
import com.roundrobin.domain.UserProfile;

public interface UserProfileService {

  public UserProfile get(String id);

  public UserProfile getByEmail(String email);

  public UserProfile save(UserProfile userProfile);

  public UserProfileTo read(String id);

  public UserProfileTo create(UserProfileTo userProfileTo);

  public UserProfileTo update(UserProfileTo userProfileTo);

  public void delete(String id);
}
