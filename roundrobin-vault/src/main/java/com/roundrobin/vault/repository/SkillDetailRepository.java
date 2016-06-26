package com.roundrobin.vault.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.vault.domain.SkillDetail;

public interface SkillDetailRepository extends MongoRepository<SkillDetail, String> {
  public Optional<SkillDetail> findById(String id);
  public List<SkillDetail> findAllByActive(boolean active);
}
