package com.roundrobin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
  public User findByUsername(String username);
}
