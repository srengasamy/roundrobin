package com.roundrobin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.SkillDetail;

public interface SkillDetailRepository extends MongoRepository<SkillDetail, String> {
  public Optional<SkillDetail> findById(String id);
}
