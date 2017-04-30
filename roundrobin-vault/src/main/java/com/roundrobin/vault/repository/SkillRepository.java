package com.roundrobin.vault.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.vault.domain.Skill;
import com.roundrobin.vault.domain.UserProfile;

public interface SkillRepository extends MongoRepository<Skill, String> {
  public Optional<Skill> findById(String id);
  public List<Skill> findAllByProfile(UserProfile profile);

}
