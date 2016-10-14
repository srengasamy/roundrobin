package com.roundrobin.map.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.roundrobin.map.domain.Trail;

public interface TrailRepository extends MongoRepository<Trail, String> {
  public Optional<Trail> findById(String id);

  public Optional<Trail> findByVendorId(String vendorId);

}
