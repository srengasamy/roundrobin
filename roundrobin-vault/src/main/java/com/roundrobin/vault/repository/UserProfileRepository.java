package com.roundrobin.vault.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.vault.domain.UserProfile;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
  public Optional<UserProfile> findById(String id);

}
