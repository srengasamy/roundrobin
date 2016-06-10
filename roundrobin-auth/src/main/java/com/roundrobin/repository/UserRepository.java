package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.roundrobin.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  public Optional<User> findById(String id);

  public Optional<User> findByUsername(String username);
}
