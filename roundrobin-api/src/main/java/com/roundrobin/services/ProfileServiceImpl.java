package com.roundrobin.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roundrobin.common.ErrorCodes;
import com.roundrobin.domain.Profile;
import com.roundrobin.repository.ProfileRepository;

@Service
public class ProfileServiceImpl implements ProfileService {
  @Autowired
  private ProfileRepository profileRepo;

  @Override
  public Profile read(String id) {
    Optional<Profile> existing = profileRepo.findById(id);
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_PROFILE_ID);
    return existing.get();
  }

  @Override
  public Profile create(Profile profile) {
    return profileRepo.save(profile);
  }

  @Override
  public Profile update(Profile profile) {
    Optional<Profile> existing = profileRepo.findById(profile.getId());
    checkArgument(existing.isPresent(), ErrorCodes.INVALID_PROFILE_ID);
    existing.get().setFirstName(profile.getFirstName());
    existing.get().setLastName(profile.getLastName());
    existing.get().setDob(profile.getDob());
    existing.get().setMobileNumber(profile.getMobileNumber());
    existing.get().setHomeNumber(profile.getHomeNumber());
    existing.get().setSex(profile.getSex());
    existing.get().setVendor(profile.getVendor());
    return profileRepo.save(existing.get());
  }

}
