package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  public Optional<Role> findById(String id);

  public Optional<Role> findByName(String name);
}
