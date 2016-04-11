package com.roundrobin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.UserAction;

public interface UserActionRepository extends MongoRepository<UserAction, String> {

}
