package com.roundrobin.services;

import com.roundrobin.domain.Profile;

public interface ProfileService {

  public Profile read(String id);

  public Profile create(Profile profile);

  public Profile update(Profile profile);
}
