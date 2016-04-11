package com.roundrobin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.SkillDetail;

public interface SkillDetailRepository extends MongoRepository<SkillDetail, String> {

}
