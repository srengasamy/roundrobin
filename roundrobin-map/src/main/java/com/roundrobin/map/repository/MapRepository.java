package com.roundrobin.map.repository;

import java.util.List;

import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.map.domain.Map;

public interface MapRepository extends MongoRepository<Map, String> {
  public List<Map> findByVendorSkillsInAndPointWithin(String skillId, Circle circle);
  public List<Map> findByPointWithinAndVendorSkillsIn(Circle circle, String skillId);
}
