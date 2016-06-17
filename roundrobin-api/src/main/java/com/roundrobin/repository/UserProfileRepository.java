package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.UserProfile;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
  public Optional<UserProfile> findById(String id);

  public Optional<UserProfile> findByEmail(String email);
}
