package com.roundrobin.vault.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.vault.domain.SkillGroup;


public interface SkillGroupRepository extends MongoRepository<SkillGroup, String> {
  public Optional<SkillGroup> findById(String id);

  public Optional<SkillGroup> findByGroupName(String groupName);

  public List<SkillGroup> findAllByActive(boolean active);
}
