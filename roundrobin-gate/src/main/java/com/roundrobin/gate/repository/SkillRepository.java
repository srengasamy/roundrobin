package com.roundrobin.gate.repository;

import com.roundrobin.gate.domain.Skill;
import com.roundrobin.gate.domain.UserProfile;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends MongoRepository<Skill, String> {
  public Optional<Skill> findById(String id);

  public List<Skill> findAllByProfile(UserProfile profile);

}
