package com.roundrobin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.domain.Skill;

public interface SkillRepository extends MongoRepository<Skill, String> {

}
