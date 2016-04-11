package com.roundrobin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.Credentials;

public interface CredentialsRepository extends MongoRepository<Credentials, String> {

}
