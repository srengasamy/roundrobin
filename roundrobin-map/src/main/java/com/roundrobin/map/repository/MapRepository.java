package com.roundrobin.map.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.map.domain.Map;
import com.roundrobin.map.domain.Vendor;

public interface MapRepository extends MongoRepository<Map, String> {

  public Optional<Map> findByVendor(Vendor vendor);

  public List<Map> findByVendorSkillsInAndPointWithin(String skillId, Circle circle);

  public List<Map> findByPointWithinAndVendorSkillsIn(Circle circle, String skillId);

  public List<Map> findByPointWithin(Circle circle);
}
