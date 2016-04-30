package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.UserAction;

public interface UserActionRepository extends MongoRepository<UserAction, String> {
  public Optional<UserAction> findById(String id);

}
