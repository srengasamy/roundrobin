package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.Credential;

public interface CredentialRepository extends MongoRepository<Credential, String> {
  public Optional<Credential> findById(String id);

}
