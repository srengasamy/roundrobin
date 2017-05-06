package com.roundrobin.gate.repository;

import com.roundrobin.gate.domain.UserProfile;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
  public Optional<UserProfile> findById(String id);

}
