package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String> {
  public Optional<Profile> findById(String id);
}
