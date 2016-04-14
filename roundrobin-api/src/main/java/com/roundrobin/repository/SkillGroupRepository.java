package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.SkillGroup;


public interface SkillGroupRepository extends MongoRepository<SkillGroup, String> {
  public Optional<SkillGroup> findById(String id);

}
