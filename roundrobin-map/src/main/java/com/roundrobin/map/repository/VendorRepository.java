package com.roundrobin.map.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.map.domain.Vendor;

public interface VendorRepository extends MongoRepository<Vendor, String> {
  public Optional<Vendor> findById(String id);

  public List<Vendor> findByLocationWithinAndSkillsIn(Circle circle, String skillId);
}
