package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.Skill;

public interface SkillRepository extends MongoRepository<Skill, String> {
  public Optional<Skill> findById(String id);

}
