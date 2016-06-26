package com.roundrobin.vault.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.vault.domain.UserAction;

public interface UserActionRepository extends MongoRepository<UserAction, String> {
  public Optional<UserAction> findById(String id);

}
