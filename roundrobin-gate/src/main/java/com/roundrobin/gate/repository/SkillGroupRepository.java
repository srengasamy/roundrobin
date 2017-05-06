package com.roundrobin.gate.repository;

import com.roundrobin.gate.domain.SkillGroup;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface SkillGroupRepository extends MongoRepository<SkillGroup, String> {
  public Optional<SkillGroup> findById(String id);

  public Optional<SkillGroup> findByGroupName(String groupName);

  public List<SkillGroup> findAllByActive(boolean active);
}
