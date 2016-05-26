package com.roundrobin.repository;

import com.roundrobin.domain.User;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by rengasu on 5/25/16.
 */
public interface UserRepository extends MongoRepository<User, String> {
  public User findByUsername(String username);
}
