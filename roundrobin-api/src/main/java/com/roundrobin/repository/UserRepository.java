package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
  public Optional<User> findById(String id);
}
