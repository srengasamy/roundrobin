package com.roundrobin.gate.repository;

import com.roundrobin.gate.domain.SkillDetail;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SkillDetailRepository extends MongoRepository<SkillDetail, String> {
  public Optional<SkillDetail> findById(String id);
  public List<SkillDetail> findAllByActive(boolean active);
}
